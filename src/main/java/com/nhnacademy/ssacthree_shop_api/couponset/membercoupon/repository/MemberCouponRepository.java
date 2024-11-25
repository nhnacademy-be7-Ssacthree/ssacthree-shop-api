package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.repository;

import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.domain.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    List<MemberCoupon> findByCustomerId(Long customerId);
}
