package com.nhnacademy.ssacthree_shop_api.commons.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nhnacademy.ssacthree_shop_api.commons.dto.ErrorResponse;
import com.nhnacademy.ssacthree_shop_api.commons.dto.OrderDetailErrorResponse;
import com.nhnacademy.ssacthree_shop_api.commons.exception.CustomException;
import com.nhnacademy.ssacthree_shop_api.commons.exception.NotFoundException;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception.OrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;


    @BeforeEach
    public void setUp() {
        handler = new GlobalExceptionHandler();
    }

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    public void testOrderDetailHandleCustomException() {
        // Given: OrderNotFoundException 생성
        Long invalidOrderId = 9999L;
        OrderNotFoundException exception = new OrderNotFoundException(invalidOrderId);

        // When: 예외 핸들러 호출
        ResponseEntity<OrderDetailErrorResponse> response =
            globalExceptionHandler.orderDetailhandleCustomExcetion(exception);

        // Then: 응답 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("해당 주문을 찾지 못함 / 주문번호: " + invalidOrderId);
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }



    @Test
    void testHandleCustomException() {
        // given
        CustomException exception = new CustomException("Custom exception occurred",
            HttpStatus.BAD_REQUEST.value());

        // when
        var response = handler.handleCustomException(exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse body = response.getBody();
        assertEquals("Custom exception occurred", body.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatusCode());
    }

    @Test
    void testHandleNotFoundException() {
        // given
        NotFoundException exception = new NotFoundException("Not Found Exception occurred");

        // when
        var response = handler.handleNotFoundException(exception);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found Exception occurred", response.getBody());
    }

    @Test
    void testHandleIllegalArgumentException() {
        // given
        IllegalArgumentException exception = new IllegalArgumentException(
            "Invalid argument provided");

        // when
        var response = handler.handleIllegalArgumentException(exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid argument provided", response.getBody());
    }

}
