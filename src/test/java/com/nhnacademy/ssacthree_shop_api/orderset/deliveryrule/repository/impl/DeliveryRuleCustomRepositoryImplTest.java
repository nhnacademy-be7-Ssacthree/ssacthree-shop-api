package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.repository.impl;

import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.QDeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleGetResponse;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeliveryRuleCustomRepositoryImplTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @InjectMocks
    private DeliveryRuleCustomRepositoryImpl deliveryRuleCustomRepository;

    private static final QDeliveryRule deliveryRule = QDeliveryRule.deliveryRule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDeliveryRules() {
        // Arrange
        List<DeliveryRuleGetResponse> mockDeliveryRules = Arrays.asList(
                new DeliveryRuleGetResponse(
                        1L,
                        "Free Delivery",
                        0,
                        0,
                        true,
                        LocalDateTime.now()
                ),
                new DeliveryRuleGetResponse(
                        2L,
                        "Discounted Delivery",
                        500,
                        100,
                        false,
                        LocalDateTime.now().plusDays(1)
                )
        );

        // Mock the query
        JPAQuery<DeliveryRuleGetResponse> mockedQuery = mock(JPAQuery.class);

        // Configure the query factory to return the mocked query
        when(queryFactory.select(Projections.constructor(
                DeliveryRuleGetResponse.class,
                deliveryRule.deliveryRuleId,
                deliveryRule.deliveryRuleName,
                deliveryRule.deliveryFee,
                deliveryRule.deliveryDiscountCost,
                deliveryRule.deliveryRuleIsSelected,
                deliveryRule.deliveryRuleCreatedAt
        ))).thenReturn(mockedQuery);

        // Set the behavior for the mocked query
        when(mockedQuery.from(deliveryRule)).thenReturn(mockedQuery);
        when(mockedQuery.orderBy(deliveryRule.deliveryRuleIsSelected.desc())).thenReturn(mockedQuery);
        when(mockedQuery.orderBy(deliveryRule.deliveryRuleCreatedAt.asc())).thenReturn(mockedQuery);
        when(mockedQuery.fetch()).thenReturn(mockDeliveryRules);

        // Act
        List<DeliveryRuleGetResponse> result = deliveryRuleCustomRepository.getAllDeliveryRules();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        DeliveryRuleGetResponse rule1 = result.get(0);
        assertEquals("Free Delivery", rule1.getDeliveryRuleName());
        assertTrue(rule1.isDeliveryRuleIsSelected());

        DeliveryRuleGetResponse rule2 = result.get(1);
        assertEquals("Discounted Delivery", rule2.getDeliveryRuleName());
        assertFalse(rule2.isDeliveryRuleIsSelected());

        // QueryFactory 및 메서드 호출 검증
        verify(queryFactory, times(1)).select((Expression<Object>) any());
        verify(mockedQuery, times(1)).from(deliveryRule);
        verify(mockedQuery, times(1)).fetch();
    }
}
