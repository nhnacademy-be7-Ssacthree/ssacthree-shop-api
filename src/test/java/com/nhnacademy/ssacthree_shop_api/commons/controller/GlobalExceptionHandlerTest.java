package com.nhnacademy.ssacthree_shop_api.commons.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nhnacademy.ssacthree_shop_api.commons.dto.ErrorResponse;
import com.nhnacademy.ssacthree_shop_api.commons.exception.CustomException;
import com.nhnacademy.ssacthree_shop_api.commons.exception.IllegalArgumentException;
import com.nhnacademy.ssacthree_shop_api.commons.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    public void setUp() {
        handler = new GlobalExceptionHandler();
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
