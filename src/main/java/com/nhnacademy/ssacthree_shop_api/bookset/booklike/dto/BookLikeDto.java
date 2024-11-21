package com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookLikeDto {
    private Long bookId;
    private Long customerId;
}
