package com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class BookTagId {
    private Long bookId;
    private Long tagId;
}
