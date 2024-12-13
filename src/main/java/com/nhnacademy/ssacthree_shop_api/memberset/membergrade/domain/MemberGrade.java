package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "member_grade")
public class MemberGrade {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberGradeId;


    public MemberGrade(String memberGradeName, boolean memberGradeIsUsed, float memberGradePointSave) {
        this.memberGradeName = memberGradeName;
        this.memberGradeIsUsed = memberGradeIsUsed;
        this.memberGradePointSave = memberGradePointSave;
    }

    @Setter
    private String memberGradeName;

    @Setter
    private boolean memberGradeIsUsed;


    private LocalDateTime memberGradeCreateAt = LocalDateTime.now();

    @Setter
    private float memberGradePointSave;


}
