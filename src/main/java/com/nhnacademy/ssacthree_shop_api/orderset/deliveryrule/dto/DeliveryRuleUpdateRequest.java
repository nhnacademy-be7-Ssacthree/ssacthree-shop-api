package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliveryRuleUpdateRequest {

    @NotNull
    private long deliveryRuleId;
}
