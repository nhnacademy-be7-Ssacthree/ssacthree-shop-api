package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter

public class MemberGradeCreateRequest {

    private String memberGradeName;

    private boolean memberGradeIsUsed;

    private Float memberGradePointSave;
}
