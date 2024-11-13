package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.repository;

import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRuleRepository extends JpaRepository<CouponRule, Long> {
}
