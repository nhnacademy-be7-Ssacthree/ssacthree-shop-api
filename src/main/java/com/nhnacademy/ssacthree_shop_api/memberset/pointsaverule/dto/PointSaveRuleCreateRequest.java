package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto;

import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PointSaveRuleCreateRequest {

    @NotBlank
    private String pointSaveRuleName;

    @NotNull
    @Min(0)
    private int pointSaveAmount;

    @NotNull
    private PointSaveType pointSaveType;
}
