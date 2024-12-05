package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.service.impl;

import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.exception.DeliveryRuleNotFoundException;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.repository.DeliveryRuleRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.service.DeliveryRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryRuleServiceImpl implements DeliveryRuleService {

    private final DeliveryRuleRepository deliveryRuleRepository;

    @Override
    public DeliveryRule createDeliveryRule(DeliveryRuleCreateRequest deliveryRuleCreateRequest) {
        DeliveryRule deliveryRule = new DeliveryRule(
            deliveryRuleCreateRequest.getDeliveryRuleName(),
            deliveryRuleCreateRequest.getDeliveryFee(),
            deliveryRuleCreateRequest.getDeliveryDiscountCost()
        );

        if (getAllDeliveryRules().isEmpty()) {
            deliveryRule.setDeliveryRuleIsSelected(true);
        }

        return deliveryRuleRepository.save(deliveryRule);
    }

    @Transactional(readOnly = true)
    @Override
    public DeliveryRule getSelectedDeliveryRule() {
        return deliveryRuleRepository.findAll()
                .stream()
                .filter(DeliveryRule::isDeliveryRuleIsSelected)
                .findFirst()
                .orElseThrow(() -> new DeliveryRuleNotFoundException("선택된 배송 규칙이 없습니다."));
    }

    @Override
    public DeliveryRule updateDeliveryRule(DeliveryRuleUpdateRequest deliveryRuleUpdateRequest) {
        Long deliveryRuleId = deliveryRuleUpdateRequest.getDeliveryRuleId();
        if (deliveryRuleId <= 0) {
            throw new IllegalArgumentException("deliveryRuleId는 0 이하일 수 없습니다.");
        }

        DeliveryRule deliveryRule = deliveryRuleRepository.findById(deliveryRuleId)
                .orElseThrow(() -> new DeliveryRuleNotFoundException(deliveryRuleId + "를 찾을 수 없습니다."));

        // 기존에 선택된 배송정책 찾아서 선택 해제
        DeliveryRule selectedDeliveryRule = getSelectedDeliveryRule();
        selectedDeliveryRule.setDeliveryRuleIsSelected(false);

        // 새로운 배송정책 선택
        deliveryRule.setDeliveryRuleIsSelected(true);

        return deliveryRuleRepository.save(deliveryRule);
    }

    @Override
    public List<DeliveryRuleGetResponse> getAllDeliveryRules() {
        return deliveryRuleRepository.getAllDeliveryRules();
    }

    @Override
    public DeliveryRuleGetResponse getCurrentDeliveryRule() {
        DeliveryRule deliveryRule = deliveryRuleRepository.findByDeliveryRuleIsSelectedTrue();
        DeliveryRuleGetResponse response = new DeliveryRuleGetResponse(
                deliveryRule.getDeliveryRuleId(),
                deliveryRule.getDeliveryRuleName(),
                deliveryRule.getDeliveryFee(),
                deliveryRule.getDeliveryDiscountCost(),
                true,
                deliveryRule.getDeliveryRuleCreatedAt());
        return response;
    }
}
