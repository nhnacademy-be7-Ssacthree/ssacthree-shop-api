package com.nhnacademy.ssacthree_shop_api.shoppingcart.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartId {
    private long customerId;
    private long bookId;
}
