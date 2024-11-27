package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service;

import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderDetailSaveRequest;

import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.dto.OrderDetailResponse;
import java.util.List;

public interface OrderDetailService {
    // TODO : 일단 void인데 주문 상세응답?
    void saveOrderDetails(Order order, List<OrderDetailSaveRequest> orderDetailList);


    OrderDetailResponse getOrderDetail(Long orderId);

}
