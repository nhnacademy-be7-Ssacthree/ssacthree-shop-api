package com.nhnacademy.ssacthree_shop_api.orderset.orderdetailpackaging.domain.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderDetailSaveRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetailpackaging.domain.service.OrderDetailPackagingService;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.domain.Packaging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailPackagingServiceImpl implements OrderDetailPackagingService {
    private Packaging packaging;

    private Order order;


    private Book book;

    private int packagingQuantity;
    public void saveOrderDetailPackaging(Order order, List<OrderDetailSaveRequest> orderDetailList) {
        // 주문 상세에서 포장 정보 빼와서 저장하기 - 일단 수량은 x
//        OrderDetailPackaging orderDetailPackaging = new OrderDetailPackaging(
//                order,
//                or
//        );
    }

    // 주문 상세 포장 정보 저장

}
