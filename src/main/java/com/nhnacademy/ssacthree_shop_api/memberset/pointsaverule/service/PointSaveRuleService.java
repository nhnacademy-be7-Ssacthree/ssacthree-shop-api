package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.service;

import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleUpdateRequest;

import java.util.List;

public interface PointSaveRuleService {

    List<PointSaveRuleGetResponse> getAllPointSaveRules();

    PointSaveRule createPointSaveRule(PointSaveRuleCreateRequest pointSaveRuleCreateRequest);

    PointSaveRule updatePointSaveRule(PointSaveRuleUpdateRequest pointSaveRuleUpdateRequest);
}
