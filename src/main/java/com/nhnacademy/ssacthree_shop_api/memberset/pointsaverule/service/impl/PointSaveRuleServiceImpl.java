package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.service.impl;

import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleInfoResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.exception.PointSaveRuleNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.repository.PointSaveRuleRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.service.PointSaveRuleService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointSaveRuleServiceImpl implements PointSaveRuleService {

    private final PointSaveRuleRepository pointSaveRuleRepository;

    @Override
    public List<PointSaveRuleGetResponse> getAllPointSaveRules() {
        return pointSaveRuleRepository.getAllPointSaveRules();
    }

    @Override
    public PointSaveRule createPointSaveRule(PointSaveRuleCreateRequest pointSaveRuleCreateRequest) {
        PointSaveRule pointSaveRule = new PointSaveRule(
            pointSaveRuleCreateRequest.getPointSaveRuleName(),
            pointSaveRuleCreateRequest.getPointSaveAmount(),
            pointSaveRuleCreateRequest.getPointSaveType()
        );

        if (getAllPointSaveRules().isEmpty()) {
            pointSaveRule.setPointSaveRuleIsSelected(true);
        }

        return pointSaveRuleRepository.save(pointSaveRule);
    }

    @Override
    public PointSaveRule updatePointSaveRule(PointSaveRuleUpdateRequest pointSaveRuleUpdateRequest) {

        Long pointSaveRuleId = pointSaveRuleUpdateRequest.getPointSaveRuleId();
        if (pointSaveRuleId <= 0) {
            throw new IllegalArgumentException("pointSaveRuleId는 0 이하일 수 없습니다.");
        }

        PointSaveRule pointSaveRule = pointSaveRuleRepository.findById(pointSaveRuleId)
                .orElseThrow(() -> new PointSaveRuleNotFoundException(pointSaveRuleId + "를 찾을 수 없습니다."));

        pointSaveRule.setPointSaveRuleIsSelected(!pointSaveRule.isPointSaveRuleIsSelected());

        return pointSaveRuleRepository.save(pointSaveRule);
    }

    @Override
    public PointSaveRuleInfoResponse getPointSaveRuleByRuleName(String pointSaveRuleName){
        PointSaveRule pointSaveRule = pointSaveRuleRepository.findByPointSaveRuleNameAndPointSaveRuleIsSelectedTrue(pointSaveRuleName);

        if(pointSaveRule==null){
            throw new NotFoundException("'"+pointSaveRuleName+"' 정책을 찾을 수 없습니다.");
        }

        return new PointSaveRuleInfoResponse(pointSaveRule);
    }
}
