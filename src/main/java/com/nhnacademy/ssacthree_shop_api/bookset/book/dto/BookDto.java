package com.nhnacademy.ssacthree_shop_api.bookset.book.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private String bookName;
    private String bookIndex;
    private String bookInfo;
    private String isbn;
    private LocalDateTime publicationDate;
    private int regularPrice;
    private int salePrice;
    private boolean isPacked;
    private int stock;
    private String bookThumbnailImageUrl;
    private int bookViewCount;
    private int bookDiscount;

}
