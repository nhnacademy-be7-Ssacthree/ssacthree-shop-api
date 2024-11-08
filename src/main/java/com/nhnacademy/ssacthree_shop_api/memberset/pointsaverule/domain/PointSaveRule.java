package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "point_save_rule")
public class PointSaveRule {

    public PointSaveRule(String pointSaveRuleName, int pointSaveAmount, PointSaveType pointSaveType) {
        this.pointSaveRuleName = pointSaveRuleName;
        this.pointSaveAmount = pointSaveAmount;
        this.pointSaveType = pointSaveType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_save_rule_id")
    private long pointSaveRuleId;

    @Size(max = 30)
    @Column(name = "point_save_rule_name")
    private String pointSaveRuleName;

    @Column(name = "point_save_amount")
    private int pointSaveAmount = 0;

    @Column(name = "point_save_rule_generate_date")
    private LocalDateTime pointSaveRuleGenerateDate = LocalDateTime.now();

    @Setter
    @Column(name = "point_save_rule_is_selected")
    private boolean pointSaveRuleIsSelected = false;

    @Enumerated(EnumType.STRING)
    private PointSaveType pointSaveType;
}
