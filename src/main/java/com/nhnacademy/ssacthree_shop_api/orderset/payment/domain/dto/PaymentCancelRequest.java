package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentCancelRequest {

    private String paymentKey;
    private String cancelReason;
}
