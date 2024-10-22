package com.nhnacademy.ssacthree_shop_api.shoppingcart.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class ShoppingCartId {
    private long customerId;
    private long bookId;
}
