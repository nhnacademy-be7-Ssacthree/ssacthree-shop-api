package com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PointHistoryGetResponse {

    private Long pointHistoryId;
    private Integer pointAmount;
    private LocalDateTime pointChangeDate;
    private String pointChangeReason;
}
