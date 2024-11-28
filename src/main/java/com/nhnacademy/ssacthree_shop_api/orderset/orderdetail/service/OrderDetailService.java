package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service;

import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderDetailSaveRequest;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface OrderDetailService {
    // TODO : 일단 void인데 주문 상세응답?
    void saveOrderDetails(Order order, List<OrderDetailSaveRequest> orderDetailList);

    Optional<Long> getOrderId(String orderNumber);
    OrderDetailResponse getOrderDetail(Long orderId);
    Boolean comparePhoneNumber(Long orderId,String phoneNumber);

    }
