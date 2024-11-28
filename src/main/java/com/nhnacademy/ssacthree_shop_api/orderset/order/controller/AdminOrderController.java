package com.nhnacademy.ssacthree_shop_api.orderset.order.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.AdminOrderResponseWithCount;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponseWithCount;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderSaveRequest;
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
    ResponseEntity<MessageResponse> changeOrderStatus(@RequestBody String orderId) {
        orderService.changeOrderstatus(orderId);
        MessageResponse messageResponse = new MessageResponse("주문 상태 변경 성공");
        return ResponseEntity.ok().body(messageResponse);
    }

}
