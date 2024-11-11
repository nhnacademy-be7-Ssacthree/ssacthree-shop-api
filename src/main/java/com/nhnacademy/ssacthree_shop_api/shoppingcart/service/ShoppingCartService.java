package com.nhnacademy.ssacthree_shop_api.shoppingcart.service;

import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressResponse;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.dto.ShoppingCartItemResponse;
import java.util.List;

public interface ShoppingCartService {

    List<ShoppingCartItemResponse> getShoppingCartItemsBycustomerId(String header);

    ShoppingCartItemResponse getBookByBookId(String bookId);
}
