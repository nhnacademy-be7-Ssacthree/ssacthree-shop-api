package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto;

import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PointSaveRuleInfoResponse {

    private Long pointSaveRuleId;
    private String pointSaveRuleName;
    private int pointSaveAmount;
    private String pointSaveType;

    public PointSaveRuleInfoResponse(PointSaveRule pointSaveRule) {
        this.pointSaveRuleId = pointSaveRule.getPointSaveRuleId();
        this.pointSaveRuleName = pointSaveRule.getPointSaveRuleName();
        this.pointSaveAmount = pointSaveRule.getPointSaveAmount();
        this.pointSaveType = pointSaveRule.getPointSaveType().toString();
    }
}
