package com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain;

import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.awt.Point;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "point_history")
public class PointHistory {

    public PointHistory(PointSaveRule pointSaveRule, Member member) {
        this.member = member;
        this.pointSaveRule = pointSaveRule;
    }

    public PointHistory(PointSaveRule pointSaveRule, Member member, int pointAmount, String pointChangeReason) {
        this.pointSaveRule = pointSaveRule;
        this.member = member;
        this.pointAmount = pointAmount;
        this.pointChangeReason = pointChangeReason;
    }


    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pointHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_save_rule_id")
    private PointSaveRule pointSaveRule;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Member member;

    @NotNull
    @Setter
    private int pointAmount;

    @NotNull
    private LocalDateTime pointChangeDate = LocalDateTime.now();

    @NotNull
    @Size(max = 50)
    @Setter
    private String pointChangeReason;
}
