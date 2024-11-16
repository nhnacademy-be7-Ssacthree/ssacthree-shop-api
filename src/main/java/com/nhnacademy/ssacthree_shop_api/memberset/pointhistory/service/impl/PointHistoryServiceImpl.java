package com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.service.impl;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistoryGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.repository.PointHistoryRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.service.PointHistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    @Override
    public MessageResponse savePointHistory() {
        return null;
    }

    @Override
    public List<PointHistoryGetResponse> getMemberPointHistories(Long cusotmerId) {
        return List.of();
    }
}
