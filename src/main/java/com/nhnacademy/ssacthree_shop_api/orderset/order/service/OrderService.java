package com.nhnacademy.ssacthree_shop_api.orderset.order.service;

import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.AdminOrderResponseWithCount;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponseWithCount;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderSaveRequest;
import java.time.LocalDateTime;

public interface OrderService {
    OrderResponse createOrder(OrderSaveRequest orderSaveRequest);

    OrderResponseWithCount getOrdersByCustomerAndDate(Long customerId, int page, int size, LocalDateTime startDate, LocalDateTime endDate);

    AdminOrderResponseWithCount adminGetAllOrders(int page, int size, LocalDateTime startDateTime, LocalDateTime endDateTime);

   boolean updateOrderStatus(Long orderId, String status);

    // OrderId로 order 객체 반환
    Order getOrder(Long orderId);
}