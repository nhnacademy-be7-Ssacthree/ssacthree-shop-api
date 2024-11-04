package com.nhnacademy.ssacthree_shop_api.bookset.book.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BookStatus {
    ON_SALE("판매 중"),
    DISCONTINUED("판매 중단"),
    OUT_OF_STOCK("재고 없음");

    private final String status;

    BookStatus(String status) {
        this.status = status;
    }

    public static BookStatus getBookStatus(final String status) {
        return Arrays.stream(BookStatus.values())
                .filter(bookStatus -> bookStatus.getStatus().equals(status))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
