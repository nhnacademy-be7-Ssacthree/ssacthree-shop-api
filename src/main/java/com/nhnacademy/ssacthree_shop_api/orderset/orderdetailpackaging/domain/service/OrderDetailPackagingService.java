package com.nhnacademy.ssacthree_shop_api.orderset.orderdetailpackaging.domain.service;

import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderDetailSaveRequest;

import java.util.List;

public interface OrderDetailPackagingService {
    void saveOrderDetailPackaging(Order order, List<OrderDetailSaveRequest> orderDetailList);
}
