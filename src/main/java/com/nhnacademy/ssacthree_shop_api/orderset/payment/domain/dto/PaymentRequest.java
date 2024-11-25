package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PaymentRequest {
    private String paymentKey;
    private String orderId;
    //결제 금액
    Integer amount;

    //결제 타입 정보 String - 일반결제 등등
    private String type;

    //결제 수단 - 카드 등등
    private String method;

    //결제 처리 상태
    private String status;

//    //결제 요청 날짜와 시간
//    private String requestesAt;

    //결제 완료 날짜와 시간 정보
    private String approveedAt;

}
