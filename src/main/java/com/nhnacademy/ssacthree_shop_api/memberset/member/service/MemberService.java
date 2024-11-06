package com.nhnacademy.ssacthree_shop_api.memberset.member.service;

import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberRegisterRequest;

public interface MemberService {

    void registerMember(MemberRegisterRequest memberCreateRequest);

    MemberInfoGetResponse getMemberInfoById(String memberLoginId);

//    void updateMember(String memberLoginId, MemberUpdateRequest memberUpdateRequest);

    void deleteMember(String memberLoginId);

}
