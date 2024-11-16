package com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.service.impl;

import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistoryGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.repository.PointHistoryRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.service.PointHistoryService;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PointHistoryServiceImpl implements PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    @Override
    public PointHistory savePointHistory(PointSaveRule pointSaveRule, Member member,
        PointHistorySaveRequest pointHistorySaveRequest) {
        PointHistory pointHistory = new PointHistory(pointSaveRule, member);
        pointHistory.setPointAmount(pointHistorySaveRequest.getPointAmount());
        pointHistory.setPointChangeReason(pointHistorySaveRequest.getReason());
        return pointHistoryRepository.save(pointHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PointHistoryGetResponse> getMemberPointHistories(Long customerId,
        Pageable pageable) {

        return null;
    }
}
