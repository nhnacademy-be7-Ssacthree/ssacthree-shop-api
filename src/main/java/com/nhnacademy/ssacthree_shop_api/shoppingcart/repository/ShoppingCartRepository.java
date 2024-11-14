package com.nhnacademy.ssacthree_shop_api.shoppingcart.repository;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.domain.ShoppingCart;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.domain.ShoppingCartId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, ShoppingCartId> {

    @Query("SELECT sc FROM ShoppingCart sc JOIN FETCH sc.book WHERE sc.customer.customerId = :customerId")
    List<ShoppingCart> findByCustomer_CustomerId(@Param("customerId") Long customerId);

    List<ShoppingCart> findAllByCustomer(Customer customer);
}
