package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.service;

import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleUpdateRequest;

import java.util.List;

public interface DeliveryRuleService {

    DeliveryRule createDeliveryRule(DeliveryRuleCreateRequest deliveryRuleCreateRequest);

    DeliveryRuleGetResponse getDeliveryRuleById(Long deliveryRuleId);

    void updateDeliveryRule(Long deliveryRuleId, DeliveryRuleUpdateRequest deliveryRuleUPdateRequest);

    void deleteDeliveryRuleById(Long deliveryRuleId);

    List<DeliveryRuleGetResponse> getAllDeliveryRules();
}
