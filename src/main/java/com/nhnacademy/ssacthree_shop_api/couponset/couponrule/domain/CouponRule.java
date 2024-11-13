package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponRule {

    public CouponRule(CouponType couponType, int couponMinOrderPrice, int maxDiscountPrice, int couponDiscountPrice, String couponRuleName) {
        this.couponType = couponType;
        this.couponMinOrderPrice = couponMinOrderPrice;
        this.maxDiscountPrice = maxDiscountPrice;
        this.couponDiscountPrice = couponDiscountPrice;
        this.couponRuleName = couponRuleName;
    }

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
    @Setter
    private boolean couponIsUsed = true;

    @NotNull
    private LocalDateTime couponRuleCreatedAt = LocalDateTime.now();

}
