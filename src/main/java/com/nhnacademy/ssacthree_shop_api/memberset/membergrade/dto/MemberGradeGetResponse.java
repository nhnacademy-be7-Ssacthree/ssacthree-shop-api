package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberGradeGetResponse {

    private Long memberGradeId;

    private String memberGradeName;

    private boolean memberGradeIsUsed;

    private LocalDateTime memberGradeCreateAt;

    private Float memberGradePointSave;
}
