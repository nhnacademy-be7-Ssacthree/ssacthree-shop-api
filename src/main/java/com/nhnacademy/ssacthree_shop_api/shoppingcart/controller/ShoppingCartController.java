package com.nhnacademy.ssacthree_shop_api.shoppingcart.controller;

import com.nhnacademy.ssacthree_shop_api.shoppingcart.dto.ShoppingCartItemResponse;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.dto.ShoppingCartRequest;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.service.ShoppingCartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/shop/carts")
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
    public ResponseEntity<ShoppingCartItemResponse> getRandomBook(@PathVariable("bookId") Long bookId) {
        ShoppingCartItemResponse cartItem = shoppingCartService.getBookByBookId(bookId);
        return ResponseEntity.ok(cartItem);
    }

    @PostMapping("cart")
    public ResponseEntity<Void> saveCart(@RequestBody List<ShoppingCartRequest> cartList, @RequestParam Long customerId) {
        shoppingCartService.saveCart(cartList,customerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/add")
    public ResponseEntity<ShoppingCartItemResponse> getBook(@RequestParam Long bookId) {
        if (bookId == null || bookId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 bookId입니다. bookId는 0보다 커야 합니다.");
        }
        ShoppingCartItemResponse cartItem = shoppingCartService.getBookByBookId(bookId);
        return ResponseEntity.ok(cartItem);
    }

    @PostMapping("logout")
    public ResponseEntity<Void> saveCartonLogout(@RequestHeader(name = "X-USER-ID") String header, @RequestBody List<ShoppingCartRequest> cartList) {
        shoppingCartService.saveCartonLogout(header,cartList);
        return ResponseEntity.ok().build();
    }

}
