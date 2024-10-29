package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.service.Impl;

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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryRuleServiceImpl implements DeliveryRuleService {

    private final DeliveryRuleRepository deliveryRuleRepository;

    @Override
    public DeliveryRule createDeliveryRule(DeliveryRuleCreateRequest deliveryRuleCreateRequest) {
        DeliveryRule deliveryRule = new DeliveryRule(
            deliveryRuleCreateRequest.getDeliveryRuleName(),
            deliveryRuleCreateRequest.getDeliveryRuleFee(),
            deliveryRuleCreateRequest.getDeliveryRuleDiscountCost(),
            deliveryRuleCreateRequest.isDeliveryRuleIsSelected(),
            deliveryRuleCreateRequest.getDeliveryRuleCreatedAt()
        );
        return deliveryRuleRepository.save(deliveryRule);
    }

    @Transactional(readOnly = true)
    @Override
    public DeliveryRuleGetResponse getDeliveryRuleById(Long deliveryRuleId) {
        if (deliveryRuleId <= 0) {
            throw new IllegalArgumentException("deliveryRuleId는 0 이하일 수 없습니다.");
        }

        if (!deliveryRuleRepository.existsById(deliveryRuleId)) {
            throw new DeliveryRuleNotFoundException(deliveryRuleId + "를 찾을 수 없습니다.");
        }

        DeliveryRule foundDeliveryRule = deliveryRuleRepository.findById(deliveryRuleId).orElse(null);

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

        if (!deliveryRuleRepository.existsById(deliveryRuleId)) {
            throw new DeliveryRuleNotFoundException(deliveryRuleId + "를 찾을 수 없습니다.");
        }

        DeliveryRule deliveryRule = deliveryRuleRepository.findById(deliveryRuleId).orElse(null);
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
        List<DeliveryRule> deliveryRuleList = deliveryRuleRepository.findAll();

        List<DeliveryRuleGetResponse> deliveryRuleGetResponseList = deliveryRuleList.stream()
            .map(deliveryRule -> new DeliveryRuleGetResponse(
                deliveryRule.getId(),
                deliveryRule.getDeliveryRuleName(),
                deliveryRule.getDeliveryFee(),
                deliveryRule.getDeliveryDiscountCost(),
                deliveryRule.isDeliveryRuleIsSelected(),
                deliveryRule.getDeliveryRuleCreatedAt()
            ))
            .collect(Collectors.toList());

        return deliveryRuleGetResponseList;
    }
}
