package com.nhnacademy.ssacthree_shop_api.memberset.member.repository;

import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoGetResponse;

public interface MemberCustomRepository {

    MemberInfoGetResponse getMemberWithCustomer(String memberLoginId);
}
