package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeliveryRuleUpdateRequest {

        private String deliveryRuleName;
        private int deliveryFee;
        private int deliveryDiscountCost;
        private boolean deliveryRuleIsSelected;
        private LocalDateTime deliveryRuleCreatedAt;
}
