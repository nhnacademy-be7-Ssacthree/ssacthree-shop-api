package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.repository.impl;

import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.QDeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.repository.DeliveryRuleCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeliveryRuleCustomRepositoryImpl implements DeliveryRuleCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<DeliveryRuleGetResponse> getAllDeliveryRules() {
        QDeliveryRule deliveryRule = QDeliveryRule.deliveryRule;

        return queryFactory
                .select(Projections.constructor(
                        DeliveryRuleGetResponse.class,
                        deliveryRule.deliveryRuleId,
                        deliveryRule.deliveryRuleName,
                        deliveryRule.deliveryFee,
                        deliveryRule.deliveryDiscountCost,
                        deliveryRule.deliveryRuleIsSelected,
                        deliveryRule.deliveryRuleCreatedAt
                ))
                .from(deliveryRule)
                .orderBy(deliveryRule.deliveryRuleIsSelected.desc())
                .orderBy(deliveryRule.deliveryRuleCreatedAt.asc())
                .fetch();
    }
}
