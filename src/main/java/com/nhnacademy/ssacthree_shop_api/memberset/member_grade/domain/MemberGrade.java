package com.nhnacademy.ssacthree_shop_api.memberset.member_grade.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name="member_grade")
public class MemberGrade {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberGradeId;

    @NotNull
    @Size(min = 1, max = 20)
    private String memberGradeName;

    @NotNull
    private boolean memberGradeIsUsed;

    @NotNull
    private LocalDateTime memberGradeCreatedAt = LocalDateTime.now();

    private float memberGradePointSave;
}
