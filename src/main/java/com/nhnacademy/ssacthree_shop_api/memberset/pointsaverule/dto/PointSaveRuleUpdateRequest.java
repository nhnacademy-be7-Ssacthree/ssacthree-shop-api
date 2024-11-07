package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PointSaveRuleUpdateRequest {

    @NotNull
    private Long pointSaveRuleId;
}
