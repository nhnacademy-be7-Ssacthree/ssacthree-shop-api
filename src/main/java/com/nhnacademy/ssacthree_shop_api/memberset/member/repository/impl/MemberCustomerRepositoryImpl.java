package com.nhnacademy.ssacthree_shop_api.memberset.member.repository.impl;

import com.nhnacademy.ssacthree_shop_api.customer.domain.QCustomer;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.QMember;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberCustomerRepositoryImpl implements MemberCustomRepository {


    private final JPAQueryFactory queryFactory;
    private final QMember qMember = QMember.member;
    private final QCustomer qCustomer = QCustomer.customer;


    @Override
    public MemberInfoGetResponse getMemberWithCustomer(String memberLoginId) {
        return queryFactory.from(qMember)
            .join(qCustomer).fetchJoin()
            .on(qMember.customer.customerId.eq(qCustomer.customerId))
            .select(Projections.fields(MemberInfoGetResponse.class,
                qMember.memberLoginId,
                        qCustomer.customerName,
                        qCustomer.customerPhoneNumber,
                        qCustomer.customerEmail,
                        qMember.memberBirthdate,
                        qMember.memberPoint
                ))
            .where(qMember.memberLoginId.eq(memberLoginId)).fetchOne();
    }
}
