package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.repository;

import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import java.util.List;

public interface MemberGradeCustomRepository {
    List<MemberGrade> findAvailableMemberGrade();
}
