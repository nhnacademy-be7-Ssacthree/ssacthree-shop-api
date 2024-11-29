package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PaymentCancelRequest {
    String orderId;
    String paymentId;
}
