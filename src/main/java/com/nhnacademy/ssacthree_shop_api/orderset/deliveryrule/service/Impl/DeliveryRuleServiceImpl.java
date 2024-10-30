package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.service.Impl;

import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.QDeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.exception.DeliveryRuleNotFoundException;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.repository.DeliveryRuleRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.service.DeliveryRuleService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryRuleServiceImpl implements DeliveryRuleService {

    @PersistenceContext
    private EntityManager entityManager;

    private final DeliveryRuleRepository deliveryRuleRepository;

    @Override
    public DeliveryRule createDeliveryRule(DeliveryRuleCreateRequest deliveryRuleCreateRequest) {
        DeliveryRule deliveryRule = new DeliveryRule(
            deliveryRuleCreateRequest.getDeliveryRuleName(),
            deliveryRuleCreateRequest.getDeliveryFee(),
            deliveryRuleCreateRequest.getDeliveryDiscountCost(),
            deliveryRuleCreateRequest.isDeliveryRuleIsSelected(),
            LocalDateTime.now()
        );
        return deliveryRuleRepository.save(deliveryRule);
    }

    @Transactional(readOnly = true)
    @Override
    public DeliveryRuleGetResponse getDeliveryRuleById(Long deliveryRuleId) {
        if (deliveryRuleId <= 0) {
            throw new IllegalArgumentException("deliveryRuleId는 0 이하일 수 없습니다.");
        }

        DeliveryRule foundDeliveryRule = deliveryRuleRepository.findById(deliveryRuleId)
                .orElseThrow(() -> new DeliveryRuleNotFoundException(deliveryRuleId + "를 찾을 수 없습니다."));

        return new DeliveryRuleGetResponse(
            foundDeliveryRule.getId(),
            foundDeliveryRule.getDeliveryRuleName(),
            foundDeliveryRule.getDeliveryFee(),
            foundDeliveryRule.getDeliveryDiscountCost(),
            foundDeliveryRule.isDeliveryRuleIsSelected(),
            foundDeliveryRule.getDeliveryRuleCreatedAt()
        );
    }

    @Override
    public void updateDeliveryRule(Long deliveryRuleId, DeliveryRuleUpdateRequest deliveryRuleUPdateRequest) {
        if (deliveryRuleId <= 0) {
            throw new IllegalArgumentException("deliveryRuleId는 0 이하일 수 없습니다.");
        }

        DeliveryRule deliveryRule = deliveryRuleRepository.findById(deliveryRuleId)
                .orElseThrow(() -> new DeliveryRuleNotFoundException(deliveryRuleId + "를 찾을 수 없습니다."));

        deliveryRule.setDeliveryRuleName(deliveryRuleUPdateRequest.getDeliveryRuleName());
        deliveryRule.setDeliveryFee(deliveryRuleUPdateRequest.getDeliveryFee());
        deliveryRule.setDeliveryDiscountCost(deliveryRuleUPdateRequest.getDeliveryDiscountCost());
        deliveryRule.setDeliveryRuleIsSelected(deliveryRuleUPdateRequest.isDeliveryRuleIsSelected());
        deliveryRule.setDeliveryRuleCreatedAt(deliveryRuleUPdateRequest.getDeliveryRuleCreatedAt());

        deliveryRuleRepository.save(deliveryRule);
    }

    @Override
    public void deleteDeliveryRuleById(Long deliveryRuleId) {
        if (deliveryRuleId <= 0) {
            throw new IllegalArgumentException("deliveryRuleId는 0 이하일 수 없습니다.");
        }

        if (!deliveryRuleRepository.existsById(deliveryRuleId)) {
            throw new DeliveryRuleNotFoundException(deliveryRuleId + "를 찾을 수 없습니다.");
        }

        deliveryRuleRepository.deleteById(deliveryRuleId);
    }

    @Override
    public List<DeliveryRuleGetResponse> getAllDeliveryRules() {
        QDeliveryRule deliveryRule = QDeliveryRule.deliveryRule;

        return new JPAQueryFactory(entityManager)
                .select(Projections.constructor(
                        DeliveryRuleGetResponse.class,
                        deliveryRule.id,
                        deliveryRule.deliveryRuleName,
                        deliveryRule.deliveryFee,
                        deliveryRule.deliveryDiscountCost,
                        deliveryRule.deliveryRuleIsSelected,
                        deliveryRule.deliveryRuleCreatedAt
                ))
                .from(deliveryRule)
                .fetch();
    }

}
