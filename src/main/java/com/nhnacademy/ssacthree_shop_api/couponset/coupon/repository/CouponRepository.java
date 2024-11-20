package com.nhnacademy.ssacthree_shop_api.couponset.coupon.repository;

import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
