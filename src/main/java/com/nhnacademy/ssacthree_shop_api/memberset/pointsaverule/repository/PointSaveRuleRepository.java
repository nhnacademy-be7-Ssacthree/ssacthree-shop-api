package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.repository;

import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointSaveRuleRepository extends JpaRepository<PointSaveRule, Long> {

    // 만약 회원가입 이라는 정책이 여러개라면? 그땐 query dsl 가야됨. 사실상 가야되는게 맞는데 일단 구현
    Optional<PointSaveRule> findPointSaveRuleByPointSaveRuleName(String pointSaveRuleName);

    PointSaveRule findByPointSaveRuleNameAndPointSaveRuleIsSelectedTrue(String pointSaveRuleName);
}
