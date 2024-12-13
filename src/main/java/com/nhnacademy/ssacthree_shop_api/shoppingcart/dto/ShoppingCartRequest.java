package com.nhnacademy.ssacthree_shop_api.shoppingcart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ShoppingCartRequest {

    private Long bookId;
    private int quantity;

}
