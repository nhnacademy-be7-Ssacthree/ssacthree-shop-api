package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberGradeGetResponse {

    private long memberGradeId;

    private String memberGradeName;

    private boolean memberGradeIsUsed;

    private LocalDateTime memberGradeCreateAt;

    private float memberGradePointSave;
}
