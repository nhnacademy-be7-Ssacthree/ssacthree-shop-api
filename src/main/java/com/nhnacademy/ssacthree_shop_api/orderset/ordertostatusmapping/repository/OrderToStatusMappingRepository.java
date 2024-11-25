package com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.repository;

import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.OrderToStatusMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderToStatusMappingRepository extends JpaRepository<OrderToStatusMapping, Long> {
}
