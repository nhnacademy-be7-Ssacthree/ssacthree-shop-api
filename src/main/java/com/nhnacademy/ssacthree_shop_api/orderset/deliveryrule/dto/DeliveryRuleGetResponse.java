package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeliveryRuleGetResponse {

    private long deliveryRuleId;
    private String deliveryRuleName;
    private long deliveryFee;
    private long deliveryDiscountCost;
    private boolean deliveryRuleIsSelected;
    private LocalDateTime deliveryRuleCreatedAt;
}
