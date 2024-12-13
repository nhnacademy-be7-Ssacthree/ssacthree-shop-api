package com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response;

import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherNameResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookListBaseResponse {
    private Long bookId;
    private String bookName;
    private LocalDateTime publicationDate;
    private int regularPrice; // 판매가
    private int salePrice; // 할인 가격
    private String bookThumbnailImageUrl;
    private int bookViewCount;
    private int bookDiscount; // 할인율
    private String bookStatus; // 도서 상태
    private PublisherNameResponse publisher;

}
