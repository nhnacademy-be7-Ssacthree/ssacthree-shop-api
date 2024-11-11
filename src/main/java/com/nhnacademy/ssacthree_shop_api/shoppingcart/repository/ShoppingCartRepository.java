package com.nhnacademy.ssacthree_shop_api.shoppingcart.repository;

import com.nhnacademy.ssacthree_shop_api.shoppingcart.domain.ShoppingCart;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.domain.ShoppingCartId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, ShoppingCartId> {
    List<ShoppingCart> findByCustomer_CustomerId(long customerId);
}
