package com.nhnacademy.ssacthree_shop_api.memberset.pointorder.domain.repository;

import com.nhnacademy.ssacthree_shop_api.memberset.pointorder.domain.PointOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointOrderRepository extends JpaRepository<PointOrder, Long> {
}
