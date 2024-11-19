package com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.repository;

import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistoryGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointHistoryCustomRepository {

    Page<PointHistoryGetResponse> findAllPointHistoryByCustomerId(Long customerId,
        Pageable pageable);
}
