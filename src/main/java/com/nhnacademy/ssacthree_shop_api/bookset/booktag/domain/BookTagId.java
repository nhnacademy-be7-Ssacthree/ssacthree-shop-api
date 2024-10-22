package com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookTagId {
    private Long bookId;
    private Long tagId;
}
