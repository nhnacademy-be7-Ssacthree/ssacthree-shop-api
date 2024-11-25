package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderSaveRequest {

    // 주문 상품 정보 -
    private List<OrderDetailSaveRequest> orderDetailList;

    private Long customerId;

    // 구매자 정보
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;

    // 배송지 정보
    private String recipientName;
    private String recipientPhone;
    private String postalCode;
    private String roadAddress;
    private String detailAddress;
    private String orderRequest;
    private LocalDate deliveryDate;

    // 포인트 사용
    private Integer pointToUse;

    // 적립 포인트

    // 순수 금액



}