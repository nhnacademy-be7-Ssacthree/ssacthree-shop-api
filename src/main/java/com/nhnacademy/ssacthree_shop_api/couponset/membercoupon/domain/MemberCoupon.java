package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.domain;

import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_coupon_id")
    private long memberCouponId;

    @NotNull
    private LocalDateTime memberCouponCreatedAt;

    @NotNull
    private LocalDateTime memberCouponExpiredAt;

    private LocalDateTime memberCouponUsedAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name="couponId")
    private Coupon coupon;

    @NotNull
    @ManyToOne
    @JoinColumn(name="customerId")
    private Member customer;

    //todo: orderDetail과 OneToOne 연결

}
