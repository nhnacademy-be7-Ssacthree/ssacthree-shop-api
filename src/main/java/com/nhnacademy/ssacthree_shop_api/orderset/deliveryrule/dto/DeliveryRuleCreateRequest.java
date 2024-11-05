package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DeliveryRuleCreateRequest {

    @NotBlank
    private String deliveryRuleName;

    @NotNull
    @Min(0)
    private int deliveryFee;

    @Min(0)
    private int deliveryDiscountCost;
}
