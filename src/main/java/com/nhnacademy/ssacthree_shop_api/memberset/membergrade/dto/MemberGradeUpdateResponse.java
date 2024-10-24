package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberGradeUpdateResponse {

    private String memberGradeName;

    private boolean memberGradeIsUsed;

    private float memberGradePointSave;

}
