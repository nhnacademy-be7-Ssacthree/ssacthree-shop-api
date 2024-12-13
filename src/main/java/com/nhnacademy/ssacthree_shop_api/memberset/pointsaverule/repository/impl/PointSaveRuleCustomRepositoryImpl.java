package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.repository.impl;

import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.QPointSaveRule;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.repository.PointSaveRuleCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PointSaveRuleCustomRepositoryImpl implements PointSaveRuleCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PointSaveRuleGetResponse> getAllPointSaveRules() {
        QPointSaveRule pointSaveRule = QPointSaveRule.pointSaveRule;

        return queryFactory
                .select(Projections.constructor(
                        PointSaveRuleGetResponse.class,
                        pointSaveRule.pointSaveRuleId,
                        pointSaveRule.pointSaveRuleName,
                        pointSaveRule.pointSaveAmount,
                        pointSaveRule.pointSaveRuleGenerateDate,
                        pointSaveRule.pointSaveRuleIsSelected,
                        pointSaveRule.pointSaveType
                ))
                .from(pointSaveRule)
                .orderBy(pointSaveRule.pointSaveRuleIsSelected.desc())
                .orderBy(pointSaveRule.pointSaveRuleGenerateDate.asc())
                .fetch();
    }
}
