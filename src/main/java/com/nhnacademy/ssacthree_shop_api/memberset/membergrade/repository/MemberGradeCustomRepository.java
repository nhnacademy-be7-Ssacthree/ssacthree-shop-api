package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.repository;

import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeGetResponse;
import java.util.List;

public interface MemberGradeCustomRepository {

    List<MemberGradeGetResponse> findAvailableMemberGrade();
}
