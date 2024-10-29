package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliveryRuleCreateRequest {

    private String deliveryRuleName;
    private int deliveryRuleFee;
    private int deliveryRuleDiscountCost;
    private boolean deliveryRuleIsSelected;
    private LocalDateTime deliveryRuleCreatedAt;
}
