package com.nhnacademy.ssacthree_shop_api.shoppingcart.service;

import com.nhnacademy.ssacthree_shop_api.shoppingcart.dto.ShoppingCartItemResponse;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.dto.ShoppingCartItemResponseWithCustomerId;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.dto.ShoppingCartRequest;
import java.util.List;

public interface ShoppingCartService {

    List<ShoppingCartItemResponse> getShoppingCartItemsBycustomerId(String header);

    ShoppingCartItemResponse getBookByBookId(Long bookId);

    void saveCart(List<ShoppingCartRequest> cartList, Long customerId);

    void saveCartonLogout(String header, List<ShoppingCartRequest> cartList);
}
