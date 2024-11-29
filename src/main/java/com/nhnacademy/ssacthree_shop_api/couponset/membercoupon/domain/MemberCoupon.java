package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.domain;

import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCoupon {

    public MemberCoupon(Coupon coupon, Member customer, LocalDateTime memberCouponExpiredAt) {
        this.coupon = coupon;
        this.customer = customer;
        this.memberCouponExpiredAt = memberCouponExpiredAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_coupon_id")
    private long memberCouponId;

    @NotNull
    private LocalDateTime memberCouponCreatedAt = LocalDateTime.now();

    @NotNull
    @Setter
    private LocalDateTime memberCouponExpiredAt;

    @Setter
    private LocalDateTime memberCouponUsedAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name="couponId")
    private Coupon coupon;

    @NotNull
    @ManyToOne
    @JoinColumn(name="customerId")
    private Member customer;
}
