package com.nhnacademy.ssacthree_shop_api.memberset.member.repository.impl;

import com.nhnacademy.ssacthree_shop_api.customer.domain.QCustomer;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.QMember;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberCustomRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.QMemberGrade;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberCustomerRepositoryImpl implements MemberCustomRepository {


    private final JPAQueryFactory queryFactory;
    private static final QMember qMember = QMember.member;
    private static final QCustomer qCustomer = QCustomer.customer;
    private static final QMemberGrade qMemberGrade = QMemberGrade.memberGrade;

    @Override
    public MemberInfoGetResponse getMemberWithCustomer(String memberLoginId) {
        return queryFactory.from(qMember)
            .join(qCustomer)
            .on(qMember.customer.customerId.eq(qCustomer.customerId))
            .join(qMemberGrade)
            .on(qMember.memberGrade.memberGradeId.eq(qMemberGrade.memberGradeId))
            .fetchJoin()
            .select(Projections.fields(MemberInfoGetResponse.class,
                qMember.customer.customerId,
                qMember.memberLoginId,
                qCustomer.customerName,
                qCustomer.customerPhoneNumber,
                qCustomer.customerEmail,
                qMember.memberBirthdate,
                qMember.memberPoint,
                qMemberGrade.memberGradeName,
                qMemberGrade.memberGradePointSave
            ))
            .where(qMember.memberLoginId.eq(memberLoginId)).fetchOne();
    }
}
