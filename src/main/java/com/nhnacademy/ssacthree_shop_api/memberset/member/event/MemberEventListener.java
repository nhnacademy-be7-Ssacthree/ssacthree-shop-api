package com.nhnacademy.ssacthree_shop_api.memberset.member.event;

import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.CouponEffectivePeriodUnit;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.repository.CouponRepository;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.service.CouponService;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponRule;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponType;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.repository.CouponRuleRepository;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.service.CouponRuleService;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto.MemberCouponCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.service.MemberCouponService;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberEventListener {

    private final MemberCouponService memberCouponService;
    private final CouponService couponService;
    private final CouponRuleService couponRuleService;
    private final CouponRepository couponRepository;
    private final CouponRuleRepository couponRuleRepository;

    private static final String WELCOME_COUPON = "Welcome 쿠폰";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMemberRegisteredEvent(MemberRegisteredEvent event) {
        Member member = event.getMember();

        try {
            issueWelcomeCoupon(member);
        } catch (Exception e) {
            log.error("Welcome 쿠폰 발급에 실패했습니다: {}", e.getMessage());
        }
    }

    public void issueWelcomeCoupon(Member member) {
        Coupon coupon = couponRepository.findByCouponName(WELCOME_COUPON)
                .orElseGet(() -> {
                    CouponRule couponRule = couponRuleRepository.findByCouponRuleName(WELCOME_COUPON)
                            .orElseGet(() -> {
                                CouponRuleCreateRequest crcr = new CouponRuleCreateRequest();
                                crcr.setCouponRuleName(WELCOME_COUPON);
                                crcr.setCouponType(CouponType.CASH);
                                crcr.setCouponMinOrderPrice(50000);
                                crcr.setMaxDiscountPrice(10000);
                                crcr.setCouponDiscountPrice(10000);

                                return couponRuleService.createCouponRule(crcr);
                            });

                    CouponCreateRequest ccr = new CouponCreateRequest();
                    ccr.setCouponName(WELCOME_COUPON);
                    ccr.setCouponDescription("회원가입 시 지급되는 쿠폰");
                    ccr.setCouponEffectivePeriod(30);
                    ccr.setCouponEffectivePeriodUnit(CouponEffectivePeriodUnit.DAY);
                    ccr.setCouponRuleId(couponRule.getId());

                    return couponService.createCoupon(ccr);
                });

        MemberCouponCreateRequest couponRequest = new MemberCouponCreateRequest(
                coupon.getCouponExpiredAt(),
                null,
                coupon.getCouponId(),
                member.getId()
        );
        memberCouponService.createMemberCoupon(couponRequest);
    }
}
