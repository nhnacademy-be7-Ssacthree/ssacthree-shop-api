package com.nhnacademy.ssacthree_shop_api.shoppingcart.controller;

import com.nhnacademy.ssacthree_shop_api.shoppingcart.dto.ShoppingCartItemResponse;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.service.ShoppingCartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/shop")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ResponseEntity<List<ShoppingCartItemResponse>> getAllCartItems(
        @RequestHeader(name = "X-USER-ID") String header) {
        List<ShoppingCartItemResponse> cartItems = shoppingCartService.getShoppingCartItemsBycustomerId(header);
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("{bookId}")
    public ResponseEntity<ShoppingCartItemResponse> getRandomBook(@PathVariable("bookId") String bookId) {
        ShoppingCartItemResponse cartItem = shoppingCartService.getBookByBookId(bookId);
        return ResponseEntity.ok(cartItem);
    }

}
