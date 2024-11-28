package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.controller;

import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.dto.OrderDetailResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/orderDetail")
public class OrderDetailController {

  private final OrderDetailService orderDetailService;

  @GetMapping
  ResponseEntity<OrderDetailResponse> getOrderDetail(@RequestParam Long orderId){
    OrderDetailResponse orderDetailResponse = orderDetailService.getOrderDetail(orderId);
    return ResponseEntity.ok().body(orderDetailResponse);
  }
}