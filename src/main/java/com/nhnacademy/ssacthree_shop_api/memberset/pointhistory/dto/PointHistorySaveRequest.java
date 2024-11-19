package com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PointHistorySaveRequest {

    private Integer pointAmount;
    private String reason;
}
