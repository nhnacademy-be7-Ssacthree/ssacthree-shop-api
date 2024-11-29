package com.nhnacademy.ssacthree_shop_api.bookset.book.dto;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long bookId;
    private String bookName;
    private String bookIndex;
    private String bookInfo;
    private String bookIsbn;
    private LocalDateTime publicationDate;
    private int regularPrice;
    private int salePrice;
    private boolean isPacked;
    private int stock;
    private String bookThumbnailImageUrl;
    private int bookViewCount;
    private int bookDiscount;
    private Publisher publisher;
    private BookStatus bookStatus;

}
