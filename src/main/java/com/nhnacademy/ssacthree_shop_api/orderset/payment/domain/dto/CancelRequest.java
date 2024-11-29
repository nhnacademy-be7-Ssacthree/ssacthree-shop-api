package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CancelRequest {
    private String cancelReason;
}
