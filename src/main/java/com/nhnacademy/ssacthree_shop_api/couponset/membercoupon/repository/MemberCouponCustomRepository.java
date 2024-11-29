package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.repository;

import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto.MemberCouponGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCouponCustomRepository {
    Page<MemberCouponGetResponse> findAllMemberCouponByCustomerId(Long customerId, Pageable pageable);
}
