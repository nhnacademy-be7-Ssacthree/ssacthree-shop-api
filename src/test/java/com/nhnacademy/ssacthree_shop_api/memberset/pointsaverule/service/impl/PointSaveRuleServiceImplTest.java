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

import java.time.LocalDateTime;
import java.util.List;
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

    @Test
    void testGetAllPointSaveRules() {
        // Given: 샘플 데이터 준비
        PointSaveRuleGetResponse rule1 = new PointSaveRuleGetResponse(
                1L,
                "Rule1",
                10,
                LocalDateTime.now(),
                true,
                PointSaveType.PERCENT
        );

        PointSaveRuleGetResponse rule2 = new PointSaveRuleGetResponse(
                2L,
                "Rule2",
                20,
                LocalDateTime.now(),
                false,
                PointSaveType.INTEGER
        );

        List<PointSaveRuleGetResponse> ruleList = List.of(rule1, rule2);

        // When: pointSaveRuleRepository의 getAllPointSaveRules 메서드가 호출될 때
        when(pointSaveRuleRepository.getAllPointSaveRules()).thenReturn(ruleList);

        // Then: 서비스 메서드가 반환하는 값 확인
        List<PointSaveRuleGetResponse> result = pointSaveRuleService.getAllPointSaveRules();

        // 검증
        assertNotNull(result);
        assertEquals(2, result.size());

        // 첫 번째 규칙 검증
        assertEquals(1L, result.getFirst().getPointSaveRuleId());
        assertEquals("Rule1", result.getFirst().getPointSaveRuleName());
        assertEquals(10, result.getFirst().getPointSaveAmount());
        assertNotNull(result.getFirst().getPointSaveRuleGenerateDate()); // 현재 시간을 확인
        assertTrue(result.get(0).isPointSaveRuleIsSelected());
        assertEquals(PointSaveType.PERCENT, result.getFirst().getPointSaveType());

        // 두 번째 규칙 검증
        assertEquals(2L, result.get(1).getPointSaveRuleId());
        assertEquals("Rule2", result.get(1).getPointSaveRuleName());
        assertEquals(20, result.get(1).getPointSaveAmount());
        assertNotNull(result.get(1).getPointSaveRuleGenerateDate()); // 현재 시간을 확인
        assertFalse(result.get(1).isPointSaveRuleIsSelected());
        assertEquals(PointSaveType.INTEGER, result.get(1).getPointSaveType());

        // pointSaveRuleRepository.getAllPointSaveRules 메서드가 정확히 한 번 호출되었는지 확인
        verify(pointSaveRuleRepository).getAllPointSaveRules();
    }

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

    @Test
    void testUpdatePointSaveRule_AlreadySelected() {
        PointSaveRuleUpdateRequest request = new PointSaveRuleUpdateRequest(1L);
        PointSaveRule existingRule = new PointSaveRule("TestRule", 10, PointSaveType.INTEGER);
        existingRule.setPointSaveRuleIsSelected(true); // 이미 선택된 상태

        when(pointSaveRuleRepository.findById(1L)).thenReturn(Optional.of(existingRule));
        when(pointSaveRuleRepository.save(any(PointSaveRule.class))).thenReturn(existingRule);

        PointSaveRule result = pointSaveRuleService.updatePointSaveRule(request);

        assertNotNull(result);
        assertFalse(result.isPointSaveRuleIsSelected()); // 선택 해제 상태로 변경되어야 함
        verify(pointSaveRuleRepository).findById(1L);
        verify(pointSaveRuleRepository).save(existingRule);
    }

    @Test
    void testCreatePointSaveRule_MultipleRules() {
        PointSaveRuleCreateRequest request = new PointSaveRuleCreateRequest();
        request.setPointSaveRuleName("ThirdRule");
        request.setPointSaveAmount(30);
        request.setPointSaveType(PointSaveType.PERCENT);

        PointSaveRule savedRule = new PointSaveRule("ThirdRule", 30, PointSaveType.PERCENT);
        savedRule.setPointSaveRuleIsSelected(false); // 기본적으로 선택되지 않음

        PointSaveRuleGetResponse existingRule = new PointSaveRuleGetResponse();

        PointSaveRuleServiceImpl spyService = Mockito.spy(pointSaveRuleService);
        java.util.List<PointSaveRuleGetResponse> existingRules = java.util.Arrays.asList(
                existingRule);

        doReturn(existingRules).when(spyService).getAllPointSaveRules();
        when(pointSaveRuleRepository.save(any(PointSaveRule.class))).thenReturn(savedRule);

        PointSaveRule result = spyService.createPointSaveRule(request);

        assertNotNull(result);
        assertEquals("ThirdRule", result.getPointSaveRuleName());
        assertEquals(30, result.getPointSaveAmount());
        assertFalse(result.isPointSaveRuleIsSelected()); // 선택되지 않음
        verify(spyService).getAllPointSaveRules();
        verify(pointSaveRuleRepository).save(any(PointSaveRule.class));
    }

//    @Test
//    void testGetPointSaveRuleByRuleNameThrowsNotFoundExceptionWhenPointSaveRuleIsNull() {
//        // Given: 테스트에 사용할 ruleName 설정
//        String pointSaveRuleName = "NonExistentRule";
//
//        // When: pointSaveRuleRepository에서 해당 ruleName에 대한 정책을 찾을 수 없을 때
//        when(pointSaveRuleRepository.findByPointSaveRuleNameAndPointSaveRuleIsSelectedTrue(pointSaveRuleName))
//                .thenReturn(null);
//
//        // Then: NotFoundException이 발생하는지 검증
//        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
//            pointSaveRuleService.getPointSaveRuleByRuleName(pointSaveRuleName);
//        });
//
//        // 예외 메시지가 예상한 대로 나오나 확인
//        assertEquals("'NonExistentRule' 정책을 찾을 수 없습니다.", thrown.getMessage());
//
//        // pointSaveRuleRepository.findByPointSaveRuleNameAndPointSaveRuleIsSelectedTrue가 호출되었는지 확인
//        verify(pointSaveRuleRepository).findByPointSaveRuleNameAndPointSaveRuleIsSelectedTrue(pointSaveRuleName);
//    }
}
