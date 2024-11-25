package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.repository;

import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {
}
