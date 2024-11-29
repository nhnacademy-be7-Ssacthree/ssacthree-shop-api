package com.nhnacademy.ssacthree_shop_api.orderset.order.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponseWithCount;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderSaveRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.PaymentCancelRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.order.service.OrderService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    ResponseEntity<OrderResponse> createOrder(@RequestBody OrderSaveRequest orderSaveRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderSaveRequest);
        return ResponseEntity.ok().body(orderResponse);
    }

    // 회원 주문 조회
    @GetMapping
    public ResponseEntity<OrderResponseWithCount> getOrders(
        @RequestParam("customerId") Long customerId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // 기본 날짜 설정 (최근 3개월)
        LocalDate now = LocalDate.now();
        if (startDate == null) {
            startDate = now.minusMonths(3);
        }
        if (endDate == null) {
            endDate = now;
        }
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        // 서비스 호출
        OrderResponseWithCount response = orderService.getOrdersByCustomerAndDate(
            customerId, page, size, startDateTime, endDateTime);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/shop/payment/cancel")
    ResponseEntity<MessageResponse> cancelPayment(@RequestBody PaymentCancelRequest request) {
        String orderId = request.getOrderId();
        String paymentKey = request.getPaymentId();

        // 비즈니스 로직 실행
        boolean success = orderService.cancelPayment(orderId, paymentKey);

        if (success) {
            return ResponseEntity.ok(new MessageResponse("결제를 취소했습니다."));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("결제 취소에 실패했습니다."));
        }
    }

}
