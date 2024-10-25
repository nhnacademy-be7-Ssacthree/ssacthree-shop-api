package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain;

import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="coupon_rule_id")
    private long id;

    @NotNull
    private CouponType couponType;

    @NotNull
    private int couponMinOrderPrice;

    @NotNull
    private int maxDiscountPrice;

    @Column(name = "coupon_discount_price")
    @NotNull
    private int couponDiscountPrice;

    @NotNull
    @Size(max = 20)
    private String couponRuleName;

    @NotNull
    private boolean couponIsUsed;

    @NotNull
    private LocalDateTime couponRuleCreatedAt;

}
