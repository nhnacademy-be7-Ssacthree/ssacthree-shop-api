package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.exception.DeliveryRuleNotFoundException;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.repository.DeliveryRuleRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DeliveryRuleServiceImplTest {

    @InjectMocks
    private DeliveryRuleServiceImpl deliveryRuleService;

    @Mock
    private DeliveryRuleRepository deliveryRuleRepository;

    @Test
    void testGetAllDeliveryRules() {
        // Given: 샘플 데이터 준비
        DeliveryRuleGetResponse rule1 = new DeliveryRuleGetResponse(
                1L,
                "Rule1",
                10000,
                1000,
                true,
                LocalDateTime.now()
        );

        DeliveryRuleGetResponse rule2 = new DeliveryRuleGetResponse(
                2L,
                "Rule2",
                20000,
                2000,
                false,
                LocalDateTime.now()
        );

        List<DeliveryRuleGetResponse> ruleList = List.of(rule1, rule2);

        // When: deliveryRuleRepository의 getAllDeliveryRules 메서드가 호출될 때
        when(deliveryRuleRepository.getAllDeliveryRules()).thenReturn(ruleList);

        // Then: 서비스 메서드가 반환하는 값 확인
        List<DeliveryRuleGetResponse> result = deliveryRuleService.getAllDeliveryRules();

        // 검증
        assertNotNull(result);
        assertEquals(2, result.size());

        // 첫 번째 규칙 검증
        assertEquals(1L, result.get(0).getDeliveryRuleId());
        assertEquals("Rule1", result.get(0).getDeliveryRuleName());
        assertEquals(10000, result.get(0).getDeliveryFee());
        assertEquals(1000, result.get(0).getDeliveryDiscountCost());
        assertTrue(result.get(0).isDeliveryRuleIsSelected());
        assertNotNull(result.get(0).getDeliveryRuleCreatedAt());

        // 두 번째 규칙 검증
        assertEquals(2L, result.get(1).getDeliveryRuleId());
        assertEquals("Rule2", result.get(1).getDeliveryRuleName());
        assertEquals(20000, result.get(1).getDeliveryFee());
        assertEquals(2000, result.get(1).getDeliveryDiscountCost());
        assertNotNull(result.get(1).getDeliveryRuleCreatedAt());
        assertFalse(result.get(1).isDeliveryRuleIsSelected());

        // deliveryRuleRepository.getAllDeliveryRules 메서드가 정확히 한 번 호출되었는지 확인
        verify(deliveryRuleRepository).getAllDeliveryRules();
    }

    @Test
    void testUpdateDeliveryRule_InvalidId() {
        // Given: 유효하지 않은 ID
        DeliveryRuleUpdateRequest request = new DeliveryRuleUpdateRequest(-1L);

        // When: 예외가 발생할 때
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            deliveryRuleService.updateDeliveryRule(request);
        });

        // Then: 예외 메시지 검증
        assertEquals("deliveryRuleId는 0 이하일 수 없습니다.", exception.getMessage());
        verifyNoInteractions(deliveryRuleRepository);
    }

    @Test
    void testUpdateDeliveryRule_NotFound() {
        // Given: 존재하지 않는 ID
        DeliveryRuleUpdateRequest request = new DeliveryRuleUpdateRequest(1L);

        when(deliveryRuleRepository.findById(1L)).thenReturn(Optional.empty());

        // When: 예외가 발생할 때
        Exception exception = assertThrows(DeliveryRuleNotFoundException.class, () -> {
            deliveryRuleService.updateDeliveryRule(request);
        });

        // Then: 예외 메시지 검증
        assertEquals("1를 찾을 수 없습니다.", exception.getMessage());
        verify(deliveryRuleRepository).findById(1L);
        verify(deliveryRuleRepository, never()).save(any(DeliveryRule.class));
    }

    @Test
    void testCreateDeliveryRule_FirstRule() {
        // Given: 첫 번째 규칙 생성 요청
        DeliveryRuleCreateRequest request = new DeliveryRuleCreateRequest();
        request.setDeliveryRuleName("FirstRule");
        request.setDeliveryFee(10000);
        request.setDeliveryDiscountCost(1000);

        DeliveryRule savedRule = new DeliveryRule("FirstRule", 10000, 1000);
        savedRule.setDeliveryRuleIsSelected(true);

        DeliveryRuleServiceImpl spyService = Mockito.spy(deliveryRuleService);
        doReturn(java.util.Collections.emptyList()).when(spyService).getAllDeliveryRules();
        when(deliveryRuleRepository.save(any(DeliveryRule.class))).thenReturn(savedRule);

        // When: 서비스 메서드 호출
        DeliveryRule result = spyService.createDeliveryRule(request);

        // Then: 결과 검증
        assertNotNull(result);
        assertEquals("FirstRule", result.getDeliveryRuleName());
        assertEquals(10000, result.getDeliveryFee());
        assertEquals(1000, result.getDeliveryDiscountCost());
        assertTrue(result.isDeliveryRuleIsSelected());
        verify(spyService).getAllDeliveryRules();
        verify(deliveryRuleRepository).save(any(DeliveryRule.class));
    }

    @Test
    void testCreateDeliveryRule_NotFirstRule() {
        // Given: 두 번째 규칙 생성 요청
        DeliveryRuleCreateRequest request = new DeliveryRuleCreateRequest();
        request.setDeliveryRuleName("SecondRule");
        request.setDeliveryFee(20000);
        request.setDeliveryDiscountCost(2000);

        DeliveryRule savedRule = new DeliveryRule("SecondRule", 20000, 2000);
        savedRule.setDeliveryRuleIsSelected(false);

        DeliveryRuleGetResponse existingRule = new DeliveryRuleGetResponse();

        DeliveryRuleServiceImpl spyService = Mockito.spy(deliveryRuleService);
        java.util.List<DeliveryRuleGetResponse> existingRules = java.util.Arrays.asList(existingRule);

        doReturn(existingRules).when(spyService).getAllDeliveryRules();
        when(deliveryRuleRepository.save(any(DeliveryRule.class))).thenReturn(savedRule);

        // When: 서비스 메서드 호출
        DeliveryRule result = spyService.createDeliveryRule(request);

        // Then: 결과 검증
        assertNotNull(result);
        assertEquals("SecondRule", result.getDeliveryRuleName());
        assertEquals(20000, result.getDeliveryFee());
        assertEquals(2000, result.getDeliveryDiscountCost());
        assertFalse(result.isDeliveryRuleIsSelected());
        verify(spyService).getAllDeliveryRules();
        verify(deliveryRuleRepository).save(any(DeliveryRule.class));
    }

    @Test
    void testGetSelectedDeliveryRule_Found() {
        // Given: 선택된 배송 규칙이 존재하는 경우
        DeliveryRule selectedRule = new DeliveryRule("SelectedRule", 15000, 1500);
        selectedRule.setDeliveryRuleIsSelected(true);

        when(deliveryRuleRepository.findAll()).thenReturn(List.of(selectedRule));

        // When: 서비스 메서드 호출
        DeliveryRule result = deliveryRuleService.getSelectedDeliveryRule();

        // Then: 선택된 규칙이 반환되는지 확인
        assertNotNull(result);
        assertTrue(result.isDeliveryRuleIsSelected());
        assertEquals("SelectedRule", result.getDeliveryRuleName());
        verify(deliveryRuleRepository).findAll();
    }

    @Test
    void testGetSelectedDeliveryRule_NotFound() {
        // Given: 선택된 배송 규칙이 없는 경우
        when(deliveryRuleRepository.findAll()).thenReturn(List.of());

        // When: 예외가 발생할 때
        Exception exception = assertThrows(DeliveryRuleNotFoundException.class, () -> {
            deliveryRuleService.getSelectedDeliveryRule();
        });

        // Then: 예외 메시지 검증
        assertEquals("선택된 배송 규칙이 없습니다.", exception.getMessage());
        verify(deliveryRuleRepository).findAll();
    }

    @Test
    void testGetCurrentDeliveryRule_Found() {
        // Given: 선택된 배송 규칙이 존재하는 경우
        DeliveryRule selectedRule = new DeliveryRule("CurrentRule", 10000, 1000);
        selectedRule.setDeliveryRuleIsSelected(true);

        when(deliveryRuleRepository.findByDeliveryRuleIsSelectedTrue()).thenReturn(selectedRule);

        // When: 서비스 메서드 호출
        DeliveryRuleGetResponse result = deliveryRuleService.getCurrentDeliveryRule();

        // Then: 선택된 규칙 정보가 반환되는지 확인
        assertNotNull(result);
        assertEquals("CurrentRule", result.getDeliveryRuleName());
        assertTrue(result.isDeliveryRuleIsSelected());
        verify(deliveryRuleRepository).findByDeliveryRuleIsSelectedTrue();
    }
}
