package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberGradeCreateRequest {

    private String memberGradeName;

    private boolean memberGradeIsUsed;

    private Float memberGradePointSave;
}
