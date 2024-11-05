package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.service.impl;

import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.QPointSaveRule;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.exception.PointSaveRuleNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.repository.PointSaveRuleRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.service.PointSaveRuleService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PointSaveRuleServiceImpl implements PointSaveRuleService {

    @PersistenceContext
    private EntityManager entityManager;

    private final PointSaveRuleRepository pointSaveRuleRepository;

    @Override
    public List<PointSaveRuleGetResponse> getAllPointSaveRules() {
        QPointSaveRule pointSaveRule = QPointSaveRule.pointSaveRule;

        return new JPAQueryFactory(entityManager)
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

    @Override
    public PointSaveRule createPointSaveRule(PointSaveRuleCreateRequest pointSaveRuleCreateRequest) {
        PointSaveRule pointSaveRule = new PointSaveRule(
            pointSaveRuleCreateRequest.getPointSaveRuleName(),
            pointSaveRuleCreateRequest.getPointSaveAmount(),
            pointSaveRuleCreateRequest.getPointSaveType()
        );

        if (getAllPointSaveRules().isEmpty()) {
            pointSaveRule.setPointSaveRuleIsSelected(true);
        }

        return pointSaveRuleRepository.save(pointSaveRule);
    }

    @Transactional(readOnly = true)
    @Override
    public PointSaveRule getSelectedPointSaveRule() {
        return pointSaveRuleRepository.findAll()
                .stream()
                .filter(PointSaveRule::isPointSaveRuleIsSelected)
                .findFirst()
                .orElseThrow(() -> new PointSaveRuleNotFoundException("선택된 포인트 적립 정책이 없습니다."));
    }
}
