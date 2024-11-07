package com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.converter.BookStatusConverter;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookInfoResponse {
    private Long bookId;
    private String bookName;
    private String bookIndex; // 목차
    private String bookInfo; // 책 설명
    private String bookIsbn;
    private LocalDateTime publicationDate;
    private int regularPrice; // 판매가
    private int salePrice; // 할인 가격
    private boolean isPacked;
    private int stock;
    private String bookThumbnailImageUrl;
    private int bookViewCount;
    private int bookDiscount; // 할인율

    private String bookStatus; // 배송 상태

    // FK
    private String publisherName;

    private List<String> categoryNameList;
    private List<String> tagNameList;
    private List<String> authorNameList;

    public BookInfoResponse(Book book, List<String> categoryNameList, List<String> tagNameList, List<String> authorNameList) {
        this.bookId = book.getBookId();
        this.bookName = book.getBookName();
        this.bookIndex = book.getBookIndex();
        this.bookInfo = book.getBookInfo();
        this.bookIsbn = book.getBookIsbn();
        this.publicationDate = book.getPublicationDate();
        this.regularPrice = book.getRegularPrice();
        this.salePrice = book.getSalePrice();
        this.isPacked = book.getIsPacked();
        this.stock = book.getStock();
        this.bookThumbnailImageUrl = book.getBookThumbnailImageUrl();
        this.bookViewCount = book.getBookViewCount();
        this.bookDiscount = book.getBookDiscount();

        BookStatusConverter converter = new BookStatusConverter();
        this.bookStatus = converter.convertToDatabaseColumn(book.getBookStatus());

        this.publisherName = book.getPublisher().getPublisherName();

        this.categoryNameList = categoryNameList;
        this.tagNameList = tagNameList;
        this.authorNameList = authorNameList;
    }

}
