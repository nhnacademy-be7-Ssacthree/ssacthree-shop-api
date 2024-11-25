package com.nhnacademy.ssacthree_shop_api.couponset.coupon.repository;

import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findCouponByCouponName(String couponName);
}
