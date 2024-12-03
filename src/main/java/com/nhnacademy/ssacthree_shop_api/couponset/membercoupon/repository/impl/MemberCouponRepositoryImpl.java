package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.repository.impl;

import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.domain.QMemberCoupon;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto.MemberCouponGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.repository.MemberCouponCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberCouponRepositoryImpl implements MemberCouponCustomRepository {

    private final JPAQueryFactory queryFactory;
    private static final QMemberCoupon qMemberCoupon = QMemberCoupon.memberCoupon;

    @Override
    public Page<MemberCouponGetResponse> findAllNotUsedMemberCouponByCustomerId(Long customerId, Pageable pageable) {
        List<MemberCouponGetResponse> memberCouponGetResponseList = queryFactory.select(
                Projections.fields(MemberCouponGetResponse.class,
                    qMemberCoupon.coupon.couponName,
                    qMemberCoupon.coupon.couponDescription,
                    qMemberCoupon.memberCouponCreatedAt,
                    qMemberCoupon.memberCouponExpiredAt,
                    qMemberCoupon.memberCouponUsedAt))
            .from(qMemberCoupon)
            .where(qMemberCoupon.customer.customer.customerId.eq(customerId)
                .and(qMemberCoupon.memberCouponUsedAt.isNull()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(memberCouponGetResponseList, pageable,
                memberCouponGetResponseList.size());
    }

    @Override
    public Page<MemberCouponGetResponse> findAllUsedMemberCouponByCustomerId(Long customerId, Pageable pageable) {
        List<MemberCouponGetResponse> memberCouponGetResponseList = queryFactory.select(
                Projections.fields(MemberCouponGetResponse.class,
                    qMemberCoupon.coupon.couponName,
                    qMemberCoupon.coupon.couponDescription,
                    qMemberCoupon.memberCouponCreatedAt,
                    qMemberCoupon.memberCouponExpiredAt,
                    qMemberCoupon.memberCouponUsedAt))
            .from(qMemberCoupon)
            .where(qMemberCoupon.customer.customer.customerId.eq(customerId)
                .and(qMemberCoupon.memberCouponUsedAt.isNotNull()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(memberCouponGetResponseList, pageable,
                memberCouponGetResponseList.size());
    }
}
