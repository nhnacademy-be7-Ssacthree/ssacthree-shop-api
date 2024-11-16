package com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.service;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistoryGetResponse;
import java.util.List;

public interface PointHistoryService {

    // 주문 취소도 주문 취소 history로 관리하는것이 좋지않을까?
    MessageResponse savePointHistory();

    // history를 불러와야 하니까는 가져오는 것도 있어야지 member의 pointHistory를 다 끌고옴.
    List<PointHistoryGetResponse> getMemberPointHistories(Long cusotmerId);
}
