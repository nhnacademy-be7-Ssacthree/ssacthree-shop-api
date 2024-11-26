package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.service.impl;

import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.repository.CouponRepository;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.domain.MemberCoupon;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto.MemberCouponCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto.MemberCouponGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto.MemberCouponUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.repository.MemberCouponRepository;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.service.MemberCouponService;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCouponServiceImpl implements MemberCouponService {

    @PersistenceContext
    private EntityManager entityManager;

    private final MemberCouponRepository memberCouponRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;

    @Override
    public List<MemberCouponGetResponse> getAllMemberCoupons(Long customerId) {
        List<MemberCoupon> memberCoupons = memberCouponRepository.findByCustomerId(customerId);

        return memberCoupons.stream()
                .map(memberCoupon -> new MemberCouponGetResponse(
                        memberCoupon.getMemberCouponId(),
                        memberCoupon.getCoupon().getCouponId(),
                        memberCoupon.getMemberCouponCreatedAt(),
                        memberCoupon.getMemberCouponExpiredAt(),
                        memberCoupon.getMemberCouponUsedAt()
                ))
                .toList();
    }

    @Override
    public MemberCoupon createMemberCoupon(MemberCouponCreateRequest memberCouponCreateRequest) {
        Member customer = memberRepository.findById(memberCouponCreateRequest.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Coupon coupon = couponRepository.findById(memberCouponCreateRequest.getCouponId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        MemberCoupon memberCoupon = new MemberCoupon(
            coupon,
            customer,
            memberCouponCreateRequest.getMemberCouponExpiredAt()
        );

        return memberCouponRepository.save(memberCoupon);
    }

    @Override
    public MemberCoupon updateMemberCoupon(MemberCouponUpdateRequest memberCouponUpdateRequest) {
        Long memberCouponId = memberCouponUpdateRequest.getMemberCouponId();
        if (memberCouponId <= 0) {
            throw new IllegalArgumentException("존재하지 않는 쿠폰입니다.");
        }

        MemberCoupon memberCoupon = memberCouponRepository.findById(memberCouponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        memberCoupon.setMemberCouponUsedAt(LocalDateTime.now());

        return memberCouponRepository.save(memberCoupon);
    }
}
