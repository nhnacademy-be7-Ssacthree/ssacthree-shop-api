package com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.repository;

import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.OrderToStatusMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderToStatusMappingRepository extends JpaRepository<OrderToStatusMapping, Long> {
    List<OrderToStatusMapping> findByOrderIdOrderByOrderStatusCreatedAtDesc(Long orderId, Pageable pageable);
}
