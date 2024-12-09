package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.repository;

import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleGetResponse;

import java.util.List;

public interface PointSaveRuleCustomRepository {
    List<PointSaveRuleGetResponse> getAllPointSaveRules();
}
