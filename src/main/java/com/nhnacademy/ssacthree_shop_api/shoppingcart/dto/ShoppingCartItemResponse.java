package com.nhnacademy.ssacthree_shop_api.shoppingcart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ShoppingCartItemResponse {

    private Long id; //도서 id

    private String title; //도서 제목

    private int quantity; // 도서 수량

    private int price; // 가격(int 수정)

    private String bookThumbnailImageUrl; //이미지

}
