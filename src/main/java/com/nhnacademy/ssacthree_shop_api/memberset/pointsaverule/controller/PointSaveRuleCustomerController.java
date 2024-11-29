package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.controller;

import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleInfoResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.service.PointSaveRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop/point-save-rules")
@RequiredArgsConstructor
public class PointSaveRuleCustomerController {

    private final PointSaveRuleService pointSaveRuleService;

    @GetMapping("/{point-save-rule-name}")
    public ResponseEntity<PointSaveRuleInfoResponse> getPointSaveRuleByRuleName(@PathVariable(name="point-save-rule-name") String ruleName){
        return ResponseEntity.ok(pointSaveRuleService.getPointSaveRuleByRuleName(ruleName));
    }
}
