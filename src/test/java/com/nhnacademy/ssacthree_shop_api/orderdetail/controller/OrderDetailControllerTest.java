package com.nhnacademy.ssacthree_shop_api.orderdetail.controller;


import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.controller.OrderDetailController;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.dto.OrderDetailDTO;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.dto.OrderDetailResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service.OrderDetailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderDetailController.class)
class OrderDetailControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderDetailService orderDetailService;

  @Test
  @WithMockUser(username = "user", roles = "USER")
  void testGetOrderDetail_Success() throws Exception {
    // Given
    OrderDetailResponse mockResponse = new OrderDetailResponse(
        1L,
        LocalDate.now(),
        "ORD123456",
        LocalDate.now(),
        "INV123",
        "John Doe",
        "01012345678",
        "Leave at the door",
        "123 Road St",
        "Apt 101",
        "12345",
        100000,
        3000,
        List.of(new OrderDetailDTO(1L, "Book Title", "image.png", 2, 50000)),
        1L,
        LocalDateTime.now(),
        97000,
        "PAY123",
        "결제 완료",
        "Credit Card"
    );

    when(orderDetailService.getOrderDetail(1L)).thenReturn(mockResponse);

    // When & Then
    mockMvc.perform(get("/api/shop/orderDetail")
            .param("orderId", "1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.orderId").value(1))
        .andExpect(jsonPath("$.receiverName").value("John Doe"));
  }

  @Test
  @WithMockUser(username = "user", roles = "USER")
  void testGetOrderId_Fail_OrderNumberNotFound() throws Exception {
    // Given
    when(orderDetailService.getOrderId("INVALID_ORDER"))
        .thenReturn(Optional.empty());

    // When & Then
    mockMvc.perform(get("/api/shop/orderDetail/orderNumber")
            .param("orderNumber", "INVALID_ORDER")
            .param("phoneNumber", "01012345678")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
}

