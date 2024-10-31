package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeliveryRuleUpdateRequest {

    @NotNull
    @Setter
    private long deliveryRuleId;
}
