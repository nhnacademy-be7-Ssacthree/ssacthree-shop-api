package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.repository;

import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleGetResponse;

import java.util.List;

public interface DeliveryRuleCustomRepository {
    List<DeliveryRuleGetResponse> getAllDeliveryRules();
}
