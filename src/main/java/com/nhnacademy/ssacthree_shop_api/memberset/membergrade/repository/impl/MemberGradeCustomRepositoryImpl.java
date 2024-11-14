package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.repository.impl;

import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.QMemberGrade;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.repository.MemberGradeCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberGradeCustomRepositoryImpl implements MemberGradeCustomRepository {

    private final JPAQueryFactory queryFactory;
    private static final QMemberGrade qMemberGrade = QMemberGrade.memberGrade;


    @Override
    public List<MemberGrade> findAvailableMemberGrade() {
        return queryFactory.selectFrom(qMemberGrade)
            .where(qMemberGrade.memberGradeIsUsed.eq(Boolean.TRUE))
            .fetch();
    }
}
