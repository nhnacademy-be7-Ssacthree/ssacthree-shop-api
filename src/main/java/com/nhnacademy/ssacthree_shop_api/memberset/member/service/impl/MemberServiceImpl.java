package com.nhnacademy.ssacthree_shop_api.memberset.member.service.impl;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.repository.CouponRepository;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto.MemberCouponCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.service.MemberCouponService;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerCreateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.service.CustomerService;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.MemberStatus;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberRegisterRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.member.exception.AlreadyMemberException;
import com.nhnacademy.ssacthree_shop_api.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberCustomRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.member.service.MemberService;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.exception.MemberGradeNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.repository.MemberGradeRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.service.PointHistoryService;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.exception.PointSaveRuleNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.repository.PointSaveRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final CustomerService customerService;
    private final MemberGradeRepository memberGradeRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberCustomRepository memberCustomRepository;
    private final PointHistoryService pointHistoryService;
    private final PointSaveRuleRepository pointSaveRuleRepository;
    private final MemberCouponService memberCouponService;
    private final CouponRepository couponRepository;

    /**
     * 회원 가입 및 회원가입 포인트 적립
     *
     * @param memberRegisterRequest
     * @return
     */
    @Override
    @Transactional
    public MessageResponse registerMember(MemberRegisterRequest memberRegisterRequest) {

        if (memberRepository.existsByMemberLoginId(memberRegisterRequest.getLoginId())) {
            throw new AlreadyMemberException("아이디가 이미 사용중입니다.");
        }

        CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest(
            memberRegisterRequest.getCustomerName(),
            memberRegisterRequest.getCustomerPhoneNumber(),
            memberRegisterRequest.getCustomerEmail()
        );
        Customer customer = customerService.createCustomer(customerCreateRequest);
        memberRegisterRequest.setLoginPassword(
            passwordEncoder.encode(memberRegisterRequest.getLoginPassword()));
        Member member = new Member(
            customer,
            memberRegisterRequest.getLoginId(),
            memberRegisterRequest.getLoginPassword(),
            memberRegisterRequest.getBirth()
        );

        MemberGrade memberGrade = memberGradeRepository.findById(1L)
            .orElseThrow(() -> new MemberGradeNotFoundException("등급을 찾을 수 없습니다."));
        member.setMemberGrade(memberGrade);
        memberRepository.save(member);
        PointSaveRule pointSaveRule = pointSaveRuleRepository.findPointSaveRuleByPointSaveRuleName(
                "회원가입")
            .orElseThrow(() -> new PointSaveRuleNotFoundException("정책이 존재하지 않습니다."));

        PointHistory savedPointHistory = pointHistoryService.savePointHistory(pointSaveRule,
            member,
            new PointHistorySaveRequest(pointSaveRule.getPointSaveAmount(), "회원 가입 포인트 적립"));
        member.setMemberPoint(member.getMemberPoint() + savedPointHistory.getPointAmount());

        issueWelcomeCoupon(member);

        return new MessageResponse("생성 성공");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void issueWelcomeCoupon(Member member) {
        try {
            Coupon coupon = couponRepository.findCouponByCouponName("Welcome 쿠폰")
                .orElseThrow(() -> new IllegalArgumentException("쿠폰을 찾을 수 없습니다."));
            MemberCouponCreateRequest couponRequest = new MemberCouponCreateRequest(
                    coupon.getCouponExpiredAt(),
                    null,
                    coupon.getCouponId(),
                    member.getId()
            );
            memberCouponService.createMemberCoupon(couponRequest);
            log.info("Welcome 쿠폰이 성공적으로 발급되었습니다.");
        } catch (Exception e) {
            log.error("Welcome 쿠폰 발급에 실패했습니다: {}", e.getMessage());
            // 쿠폰 발급 실패에 대한 추가 처리 (필요 시)
        }
    }

    @Transactional(readOnly = true)
    @Override
    public MemberInfoGetResponse getMemberInfoById(String memberLoginId) {
        return memberCustomRepository.getMemberWithCustomer(
            memberLoginId);
    }

    /**
     * 멤버 정보 업데이트 메소드
     *
     * @param memberLoginId
     * @param memberInfoUpdateRequest
     * @return
     */
    @Override
    public MessageResponse updateMember(String memberLoginId,
        MemberInfoUpdateRequest memberInfoUpdateRequest) {
        // Hibernate --> 기본키를 Set 하는 경우 에러를 발생 시킴, 그래서 멤버를 빼와서 변경시키는 방향으로 수정 함 ㅠ
        Member member = memberRepository.findByMemberLoginId(memberLoginId)
            .orElseThrow(() -> new MemberNotFoundException("멤버를 찾을 수 없습니다."));

        Customer customer = member.getCustomer();
        customer.setCustomerName(memberInfoUpdateRequest.getCustomerName());
        customer.setCustomerPhoneNumber(memberInfoUpdateRequest.getCustomerPhoneNumber());
        customer.setCustomerEmail(memberInfoUpdateRequest.getCustomerEmail());

        memberRepository.save(member);

        return new MessageResponse("생성 성공");
    }

    /**
     * soft delete
     *
     * @param memberLoginId
     * @return
     */
    @Override
    public MessageResponse deleteMember(String memberLoginId) {
        Member member = memberRepository.findByMemberLoginId(memberLoginId)
            .orElseThrow(() -> new MemberNotFoundException("멤버를 찾을 수 없습니다."));

        member.setMemberStatus(MemberStatus.WITHDRAW);
        member.setPaycoIdNumber(null);
        memberRepository.save(member);

        return new MessageResponse("삭제 성공");
    }

    @Override
    public Long getCustomerIdByMemberLoginId(String memberId) {
        Member member = memberRepository.findByMemberLoginId(memberId).orElse(null);

        assert member != null;
        return member.getId();
    }
}
