package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.repository;

import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRuleRepository extends JpaRepository<CouponRule, Long>, CouponRuleCustomRepository {
    Optional<CouponRule> findByCouponRuleName(String couponRuleName);
}
