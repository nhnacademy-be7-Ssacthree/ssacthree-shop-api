package com.nhnacademy.ssacthree_shop_api.orderset.order.service;

import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderSaveRequest;

public interface OrderService {
    OrderResponse createOrder(OrderSaveRequest orderSaveRequest);
}
