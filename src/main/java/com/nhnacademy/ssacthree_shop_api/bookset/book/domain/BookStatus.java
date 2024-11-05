package com.nhnacademy.ssacthree_shop_api.bookset.book.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BookStatus {
    ON_SALE("판매 중"),
    NO_STOCK("재고 없음"),
    DISCONTINUED("판매 중단"),
    DELETE_BOOK("삭제 도서");

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
