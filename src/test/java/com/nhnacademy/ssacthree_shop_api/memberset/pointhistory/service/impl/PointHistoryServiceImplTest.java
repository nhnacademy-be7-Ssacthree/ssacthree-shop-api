package com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistoryGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.repository.PointHistoryCustomRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.repository.PointHistoryRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;


class PointHistoryServiceImplTest {

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Mock
    private PointHistoryCustomRepository pointHistoryCustomRepository;

    @InjectMocks
    private PointHistoryServiceImpl pointHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePointHistory() {
        // Given
        PointSaveRule pointSaveRule = mock(PointSaveRule.class);
        Member member = mock(Member.class);
        PointHistorySaveRequest request = new PointHistorySaveRequest(100, "Purchase");

        PointHistory savedPointHistory = new PointHistory(pointSaveRule, member);
        savedPointHistory.setPointAmount(100);
        savedPointHistory.setPointChangeReason("Purchase");

        when(pointHistoryRepository.save(any(PointHistory.class)))
            .thenReturn(savedPointHistory);

        // When
        PointHistory result = pointHistoryService.savePointHistory(pointSaveRule, member, request);

        // Then
        assertThat(result.getPointAmount()).isEqualTo(100);
        assertThat(result.getPointChangeReason()).isEqualTo("Purchase");
        verify(pointHistoryRepository).save(any(PointHistory.class));
    }

    @Test
    void getMemberPointHistories() {
        // Given
        Long customerId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);
        PointHistoryGetResponse response = new PointHistoryGetResponse(100, LocalDateTime.now(),
            "Purchase");
        Page<PointHistoryGetResponse> mockPage = new PageImpl<>(
            Collections.singletonList(response), pageable, 1);

        when(pointHistoryCustomRepository.findAllPointHistoryByCustomerId(customerId, pageable))
            .thenReturn(mockPage);

        // When
        Page<PointHistoryGetResponse> result = pointHistoryService.getMemberPointHistories(
            customerId, pageable);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getPointAmount()).isEqualTo(100);
        verify(pointHistoryCustomRepository).findAllPointHistoryByCustomerId(customerId, pageable);
    }
}