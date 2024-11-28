package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.controller;

import com.nhnacademy.ssacthree_shop_api.commons.exception.NotFoundException;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.dto.OrderDetailResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/orderDetail")
public class OrderDetailController {

  private final OrderDetailService orderDetailService;

  // orderId로 조회 (마이페이지에서 접근)
  @GetMapping
  ResponseEntity<OrderDetailResponse> getOrderDetail(@RequestParam Long orderId){
    OrderDetailResponse orderDetailResponse = orderDetailService.getOrderDetail(orderId);
    return ResponseEntity.ok().body(orderDetailResponse);
  }

  // orderNumber로 조회 (주문 조회 창에서 접근), + 전화번호 일치하는지 검증
  @GetMapping("/orderNumber")
  ResponseEntity<OrderDetailResponse> getOrderId(@RequestParam String orderNumber, @RequestParam String phoneNumber){
    log.info("OrderNumber로 주문상세를 조회합니다.");
    // 1. orderNumber 로 orderId 조회
    Long orderId = orderDetailService.getOrderId(orderNumber)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문번호입니다.: " + orderNumber));

    // 2. 주문 내역의 전화번호와 일치하는지 확인
    // 주문번호로 조회한 주문의 전화번호와 일치하지 않을 때
    log.info("전화번호를 비교합니다.");
    if(!orderDetailService.comparePhoneNumber(orderId, phoneNumber)) {
      // 커스텀 예외를 해야하나?
      throw new NotFoundException("주문번호와 전화번호가 일치하지 않습니다.");
    }

    // 3. OrderId로 주문상세 받아옴.
    log.info("주문상세를 가져옵니다.");
    OrderDetailResponse orderDetailResponse = orderDetailService.getOrderDetail(orderId);

    return ResponseEntity.ok().body(orderDetailResponse);
  }

}
