package com.nhnacademy.ssacthree_shop_api.customer.repository;


import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
