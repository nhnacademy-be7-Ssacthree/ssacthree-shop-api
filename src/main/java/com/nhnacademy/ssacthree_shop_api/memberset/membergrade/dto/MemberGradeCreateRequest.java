package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
// request에 setter가 왜 필요해용? 불필요한 어노테이션이 많아요
// - 천보성
public class MemberGradeCreateRequest {

    private String memberGradeName;

    private boolean memberGradeIsUsed;

    private float memberGradePointSave;
}
