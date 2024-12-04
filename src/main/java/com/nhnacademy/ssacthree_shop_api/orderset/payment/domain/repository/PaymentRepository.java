package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.repository;

import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
  Optional<Payment> findByOrder(Order order);

}
