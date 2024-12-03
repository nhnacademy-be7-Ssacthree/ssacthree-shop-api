package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberGradeUpdateResponse {

    private String memberGradeName;

    private boolean memberGradeIsUsed;

    private float memberGradePointSave;

}
