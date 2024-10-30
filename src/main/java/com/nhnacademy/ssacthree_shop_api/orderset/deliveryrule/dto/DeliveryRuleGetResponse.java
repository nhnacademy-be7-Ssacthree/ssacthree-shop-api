package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class DeliveryRuleGetResponse {

    private long deliveryRuleId;
    private String deliveryRuleName;
    private int deliveryFee;
    private int deliveryDiscountCost;
    private boolean deliveryRuleIsSelected;
    private LocalDateTime deliveryRuleCreatedAt;
}
