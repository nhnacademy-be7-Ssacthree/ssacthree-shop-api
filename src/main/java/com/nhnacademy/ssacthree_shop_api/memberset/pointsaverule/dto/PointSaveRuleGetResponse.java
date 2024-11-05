package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto;

import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PointSaveRuleGetResponse {

    private long pointSaveRuleId;
    private String pointSaveRuleName;
    private int pointSaveAmount;
    private LocalDateTime pointSaveRuleGenerateDate;
    private boolean pointSaveRuleIsSelected;
    private PointSaveType pointSaveType;
}
