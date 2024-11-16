package com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.repository;

import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findByMemberId(Long memberId);
}
