package com.nhnacademy.ssacthree_shop_api.orderset.order.repository;

import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepositoryCustom {
  Page<OrderResponse> findOrdersByCustomerAndDate(Long customerId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}

