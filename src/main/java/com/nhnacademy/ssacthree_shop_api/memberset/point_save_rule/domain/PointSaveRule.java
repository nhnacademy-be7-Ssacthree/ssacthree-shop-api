package com.nhnacademy.ssacthree_shop_api.memberset.point_save_rule.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class PointSaveRule {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pointSaveRuleId;

    @NotNull
    @Size(max = 30)
    private String pointSaveRuleName;

    @NotNull
    private int pointSaveAmount = 0;

    @NotNull
    private LocalDateTime pointSaveRuleGenerateDate = LocalDateTime.now();

    @NotNull
    private boolean pointSaveRuleIsSelected;


    @Enumerated(EnumType.STRING)
    private PointSaveType pointSaveType;
}
