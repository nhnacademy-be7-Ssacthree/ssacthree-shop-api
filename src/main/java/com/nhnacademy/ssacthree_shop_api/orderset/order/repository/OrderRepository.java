package com.nhnacademy.ssacthree_shop_api.orderset.order.repository;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long>{

    List<Order> findOrderByCustomer(Customer customer);
}
