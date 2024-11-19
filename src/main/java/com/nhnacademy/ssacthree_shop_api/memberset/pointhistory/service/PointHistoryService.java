package com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.service;

import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistoryGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointHistoryService {

    // 주문 취소도 주문 취소 history로 관리하는것이 좋지않을까?
    PointHistory savePointHistory(PointSaveRule pointSaveRule, Member member,
        PointHistorySaveRequest pointHistorySaveRequest);

    // history를 불러와야 하니까는 가져오는 것도 있어야지 member의 pointHistory를 다 끌고옴.
    Page<PointHistoryGetResponse> getMemberPointHistories(Long customerId, Pageable pageable);
}
