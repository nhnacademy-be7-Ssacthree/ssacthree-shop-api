package com.nhnacademy.ssacthree_shop_api.orderset.order.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.*;
import com.nhnacademy.ssacthree_shop_api.orderset.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public AdminOrderResponseWithCount adminGetAllOrders(
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
        AdminOrderResponseWithCount response = orderService.adminGetAllOrders(
                page, size, startDateTime, endDateTime);

        return ResponseEntity.ok(response).getBody();
    }

    @PostMapping("/change")
    public ResponseEntity<MessageResponse> changeOrderStatus(@RequestBody ChangeOrderStatusRequest request) {
        Long orderId = request.getOrderId();
        String status = request.getStatus();

        // 비즈니스 로직 실행
        boolean success = orderService.updateOrderStatus(orderId, status);

        if (success) {
            return ResponseEntity.ok(new MessageResponse("주문 상태가 성공적으로 변경 되었습니다."));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("주문 상태 변경에 실패했습니다."));
        }
    }
}
