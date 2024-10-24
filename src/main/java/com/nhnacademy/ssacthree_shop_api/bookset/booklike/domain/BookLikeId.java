package com.nhnacademy.ssacthree_shop_api.bookset.booklike.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class BookLikeId {
    private long bookId;
    private long customerId;
}
