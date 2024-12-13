package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class PaymentRequest {
    // db에서의 order의 Id
    private Long orderId;

    //결제 수단 - 카드 등등 -- 이거 넣기 일단 int로
    private Long method;

    //결제 금액
    private Integer amount;

    //결제 처리 상태
    private String status;

    // 결제 승인 번호 - toss에서 제공
    private String paymentKey;

    //결제 완료 날짜와 시간 정보
    private String approvedAt;

}
