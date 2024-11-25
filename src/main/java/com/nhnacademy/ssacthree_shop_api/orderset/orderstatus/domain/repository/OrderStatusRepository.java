package com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.repository;

import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
}
