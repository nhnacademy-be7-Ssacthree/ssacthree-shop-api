package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveType;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleInfoResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.exception.PointSaveRuleNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.repository.PointSaveRuleRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointSaveRuleServiceImplTest {

    @InjectMocks
    private PointSaveRuleServiceImpl pointSaveRuleService;

    @Mock
    private PointSaveRuleRepository pointSaveRuleRepository;

    @Mock
    private EntityManager entityManager;


    @Test
    void testUpdatePointSaveRule_Success() {

        PointSaveRuleUpdateRequest request = new PointSaveRuleUpdateRequest(1L);
        PointSaveRule existingRule = new PointSaveRule("TestRule", 10, PointSaveType.INTEGER);
        existingRule.setPointSaveRuleIsSelected(false);

        when(pointSaveRuleRepository.findById(1L)).thenReturn(Optional.of(existingRule));
        when(pointSaveRuleRepository.save(any(PointSaveRule.class))).thenReturn(existingRule);

        PointSaveRule result = pointSaveRuleService.updatePointSaveRule(request);

        assertNotNull(result);
        assertTrue(result.isPointSaveRuleIsSelected());
        verify(pointSaveRuleRepository).findById(1L);
        verify(pointSaveRuleRepository).save(existingRule);
    }

    @Test
    void testUpdatePointSaveRule_InvalidId() {

        PointSaveRuleUpdateRequest request = new PointSaveRuleUpdateRequest(-1L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pointSaveRuleService.updatePointSaveRule(request);
        });

        assertEquals("pointSaveRuleId는 0 이하일 수 없습니다.", exception.getMessage());
        verifyNoInteractions(pointSaveRuleRepository);
    }

    @Test
    void testUpdatePointSaveRule_NotFound() {

        PointSaveRuleUpdateRequest request = new PointSaveRuleUpdateRequest(1L);

        when(pointSaveRuleRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PointSaveRuleNotFoundException.class, () -> {
            pointSaveRuleService.updatePointSaveRule(request);
        });

        assertEquals("1를 찾을 수 없습니다.", exception.getMessage());
        verify(pointSaveRuleRepository).findById(1L);
        verify(pointSaveRuleRepository, never()).save(any(PointSaveRule.class));
    }


    @Test
    void testCreatePointSaveRule_FirstRule() {

        PointSaveRuleCreateRequest request = new PointSaveRuleCreateRequest();
        request.setPointSaveRuleName("FirstRule");
        request.setPointSaveAmount(10);
        request.setPointSaveType(PointSaveType.INTEGER);

        PointSaveRule savedRule = new PointSaveRule("FirstRule", 10, PointSaveType.INTEGER);
        savedRule.setPointSaveRuleIsSelected(true);

        PointSaveRuleServiceImpl spyService = Mockito.spy(pointSaveRuleService);
        doReturn(java.util.Collections.emptyList()).when(spyService).getAllPointSaveRules();
        when(pointSaveRuleRepository.save(any(PointSaveRule.class))).thenReturn(savedRule);

        PointSaveRule result = spyService.createPointSaveRule(request);

        assertNotNull(result);
        assertEquals("FirstRule", result.getPointSaveRuleName());
        assertEquals(10, result.getPointSaveAmount());
        assertTrue(result.isPointSaveRuleIsSelected());
        verify(spyService).getAllPointSaveRules();
        verify(pointSaveRuleRepository).save(any(PointSaveRule.class));
    }

    @Test
    void testCreatePointSaveRule_NotFirstRule() {

        PointSaveRuleCreateRequest request = new PointSaveRuleCreateRequest();
        request.setPointSaveRuleName("SecondRule");
        request.setPointSaveAmount(20);
        request.setPointSaveType(PointSaveType.PERCENT);

        PointSaveRule savedRule = new PointSaveRule("SecondRule", 20, PointSaveType.PERCENT);
        savedRule.setPointSaveRuleIsSelected(false);

        PointSaveRuleGetResponse existingRule = new PointSaveRuleGetResponse();

        PointSaveRuleServiceImpl spyService = Mockito.spy(pointSaveRuleService);
        java.util.List<PointSaveRuleGetResponse> existingRules = java.util.Arrays.asList(
            existingRule);

        doReturn(existingRules).when(spyService).getAllPointSaveRules();
        when(pointSaveRuleRepository.save(any(PointSaveRule.class))).thenReturn(savedRule);

        PointSaveRule result = spyService.createPointSaveRule(request);

        assertNotNull(result);
        assertEquals("SecondRule", result.getPointSaveRuleName());
        assertEquals(20, result.getPointSaveAmount());
        assertFalse(result.isPointSaveRuleIsSelected());
        verify(spyService).getAllPointSaveRules();
        verify(pointSaveRuleRepository).save(any(PointSaveRule.class));
    }


    @Test
    void testGetPointSaveRuleByRuleName_Success() {

        String ruleName = "TestRule";
        PointSaveRule existingRule = new PointSaveRule("TestRule", 10, PointSaveType.PERCENT);
        existingRule.setPointSaveRuleIsSelected(true);

        when(pointSaveRuleRepository.findByPointSaveRuleNameAndPointSaveRuleIsSelectedTrue(
            ruleName)).thenReturn(existingRule);

        PointSaveRuleInfoResponse result = pointSaveRuleService.getPointSaveRuleByRuleName(
            ruleName);

        assertNotNull(result);
        assertEquals("TestRule", result.getPointSaveRuleName());
        assertEquals(10, result.getPointSaveAmount());
        assertEquals(PointSaveType.PERCENT.toString(), result.getPointSaveType());
        verify(pointSaveRuleRepository).findByPointSaveRuleNameAndPointSaveRuleIsSelectedTrue(
            ruleName);
    }


}
