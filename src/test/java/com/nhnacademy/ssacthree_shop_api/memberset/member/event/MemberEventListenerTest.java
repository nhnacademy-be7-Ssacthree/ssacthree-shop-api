package com.nhnacademy.ssacthree_shop_api.memberset.member.event;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MemberEventListenerTest {

    @InjectMocks
    private MemberEventListener memberEventListener;

    @Mock
    private MemberCouponService memberCouponService;

    @Mock
    private CouponService couponService;

    @Mock
    private CouponRuleService couponRuleService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponRuleRepository couponRuleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 쿠폰과 쿠폰 룰이 모두 존재하지 않는 경우를 테스트합니다. 새로운 쿠폰 룰과 쿠폰을 생성하고, 회원에게 쿠폰을 발급해야 합니다.
     */
    @Test
    void testHandleMemberRegisteredEvent_CouponAndRuleDoNotExist() {
        // Given
        Member member = new Member();
        MemberRegisteredEvent event = new MemberRegisteredEvent(member);

        when(couponRepository.findByCouponName("Welcome 쿠폰"))
            .thenReturn(Optional.empty());

        when(couponRuleRepository.findByCouponRuleName("Welcome 쿠폰"))
            .thenReturn(Optional.empty());

        CouponRuleCreateRequest couponRuleCreateRequest = new CouponRuleCreateRequest();
        couponRuleCreateRequest.setCouponRuleName("Welcome 쿠폰");
        couponRuleCreateRequest.setCouponType(CouponType.CASH);
        couponRuleCreateRequest.setCouponMinOrderPrice(50000);
        couponRuleCreateRequest.setMaxDiscountPrice(10000);
        couponRuleCreateRequest.setCouponDiscountPrice(10000);

        CouponRule couponRule = new CouponRule(
            CouponType.CASH,
            50000,
            10000,
            10000,
            "Welcome 쿠폰"
        );

        when(couponRuleService.createCouponRule(any(CouponRuleCreateRequest.class)))
            .thenReturn(couponRule);

        CouponCreateRequest couponCreateRequest = new CouponCreateRequest();
        couponCreateRequest.setCouponName("Welcome 쿠폰");
        couponCreateRequest.setCouponDescription("회원가입 시 지급되는 쿠폰");
        couponCreateRequest.setCouponEffectivePeriod(30);
        couponCreateRequest.setCouponEffectivePeriodUnit(CouponEffectivePeriodUnit.DAY);
        couponCreateRequest.setCouponRuleId(couponRule.getId());

        Coupon coupon = new Coupon(
            "Welcome 쿠폰",
            "회원가입 시 지급되는 쿠폰",
            30,
            CouponEffectivePeriodUnit.DAY,
            null,
            couponRule
        );

        when(couponService.createCoupon(any(CouponCreateRequest.class)))
            .thenReturn(coupon);

        // When
        memberEventListener.handleMemberRegisteredEvent(event);

        // Then
        verify(couponRuleService, times(1)).createCouponRule(any(CouponRuleCreateRequest.class));
        verify(couponService, times(1)).createCoupon(any(CouponCreateRequest.class));
        verify(memberCouponService, times(1)).createMemberCoupon(
            any(MemberCouponCreateRequest.class));
    }

    /**
     * 쿠폰은 존재하지만, 쿠폰 룰이 존재하지 않는 경우를 테스트합니다. 이 상황은 일반적으로 발생하지 않지만, 시스템의 견고성을 위해 처리해야 합니다.
     */
    @Test
    void testHandleMemberRegisteredEvent_CouponExistsButRuleDoesNotExist() {
        // Given
        Member member = new Member();

        MemberRegisteredEvent event = new MemberRegisteredEvent(member);

        CouponRule couponRule = new CouponRule(
            CouponType.CASH,
            50000,
            10000,
            10000,
            "Welcome 쿠폰"
        );

        Coupon coupon = new Coupon(
            "Welcome 쿠폰",
            "회원가입 시 지급되는 쿠폰",
            30,
            CouponEffectivePeriodUnit.DAY,
            null,
            couponRule
        );

        when(couponRepository.findByCouponName("Welcome 쿠폰"))
            .thenReturn(Optional.of(coupon));

        // When
        memberEventListener.handleMemberRegisteredEvent(event);

        // Then
        verify(couponRuleService, never()).createCouponRule(any(CouponRuleCreateRequest.class));
        verify(couponService, never()).createCoupon(any(CouponCreateRequest.class));
        verify(memberCouponService, times(1)).createMemberCoupon(
            any(MemberCouponCreateRequest.class));
    }

    /**
     * 쿠폰과 쿠폰 룰이 모두 존재하는 경우를 테스트합니다. 새로운 생성 없이 바로 회원에게 쿠폰을 발급해야 합니다.
     */
    @Test
    void testHandleMemberRegisteredEvent_CouponAndRuleExist() {
        // Given
        Member member = new Member();

        MemberRegisteredEvent event = new MemberRegisteredEvent(member);

        CouponRule couponRule = new CouponRule(
            CouponType.CASH,
            50000,
            10000,
            10000,
            "Welcome 쿠폰"
        );

        Coupon coupon = new Coupon(
            "Welcome 쿠폰",
            "회원가입 시 지급되는 쿠폰",
            30,
            CouponEffectivePeriodUnit.DAY,
            null,
            couponRule
        );

        when(couponRepository.findByCouponName("Welcome 쿠폰"))
            .thenReturn(Optional.of(coupon));

        when(couponRuleRepository.findByCouponRuleName("Welcome 쿠폰"))
            .thenReturn(Optional.of(couponRule));

        // When
        memberEventListener.handleMemberRegisteredEvent(event);

        // Then
        verify(couponRuleService, never()).createCouponRule(any(CouponRuleCreateRequest.class));
        verify(couponService, never()).createCoupon(any(CouponCreateRequest.class));
        verify(memberCouponService, times(1)).createMemberCoupon(
            any(MemberCouponCreateRequest.class));
    }

    /**
     * 쿠폰 발급 중 예외가 발생하는 경우를 테스트합니다. 로그에 에러 메시지가 출력되어야 합니다.
     */
    @Test
    void testHandleMemberRegisteredEvent_ExceptionOccurs() {
        // Given
        Member member = new Member();

        MemberRegisteredEvent event = new MemberRegisteredEvent(member);

        when(couponRepository.findByCouponName("Welcome 쿠폰"))
            .thenThrow(new RuntimeException("Database error"));

        // When
        memberEventListener.handleMemberRegisteredEvent(event);

        // Then
        verify(couponRuleService, never()).createCouponRule(any(CouponRuleCreateRequest.class));
        verify(couponService, never()).createCoupon(any(CouponCreateRequest.class));
        verify(memberCouponService, never()).createMemberCoupon(
            any(MemberCouponCreateRequest.class));
    }
}
