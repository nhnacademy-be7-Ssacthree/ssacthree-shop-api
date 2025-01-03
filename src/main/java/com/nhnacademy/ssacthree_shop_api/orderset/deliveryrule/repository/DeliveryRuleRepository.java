package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.repository;

import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRuleRepository extends JpaRepository<DeliveryRule, Long>, DeliveryRuleCustomRepository {
    DeliveryRule findByDeliveryRuleIsSelectedTrue();
}
