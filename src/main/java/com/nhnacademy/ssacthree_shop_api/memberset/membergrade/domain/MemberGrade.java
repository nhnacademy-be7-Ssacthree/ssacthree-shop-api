package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    // table은 jpa로 생성하나요?
    // ddl로 미리 테이블은 안만드는 건가요?
    @Size(min = 1, max = 20)
    @Setter
    private String memberGradeName;

    @Setter
    private boolean memberGradeIsUsed;


    private LocalDateTime memberGradeCreateAt = LocalDateTime.now();

    @Setter
    private float memberGradePointSave;


}
