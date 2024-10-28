package com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain;

import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponRule;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.domain.MemberCoupon;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="coupon_id")
    private long couponId;

    @NotNull
    @Size(max = 30)
    private String couponName;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String couponDescription;

    private int couponEffectivePeriod;

    private CouponEffectivePeriodUnit couponEffectivePeriodUnit;

    @NotNull
    private LocalDateTime couponCreateAt;

    private LocalDateTime couponExpiredAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "couponRuleId")
    private CouponRule couponRule;
}
