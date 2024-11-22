package com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response;

import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BookDetailResponse {
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

    private String bookStatus; // 도서 상태

    // FK
    @Setter
    private PublisherNameResponse publisher;

    @Setter
    private List<CategoryNameResponse> categories;

    @Setter
    private List<TagInfoResponse> tags;

    @Setter
    private List<AuthorNameResponse> authors;

    private Long likeCount; // 좋아요 수

    public BookDetailResponse(BookInfoResponse bookInfoResponse, Long likeCount) {
        this.bookId = bookInfoResponse.getBookId();
        this.bookName = bookInfoResponse.getBookName();
        this.bookIndex = bookInfoResponse.getBookIndex();
        this.bookInfo = bookInfoResponse.getBookInfo();
        this.bookIsbn = bookInfoResponse.getBookIsbn();
        this.publicationDate = bookInfoResponse.getPublicationDate();
        this.regularPrice = bookInfoResponse.getRegularPrice();
        this.salePrice = bookInfoResponse.getSalePrice();
        this.isPacked = bookInfoResponse.isPacked();
        this.stock = bookInfoResponse.getStock();
        this.bookThumbnailImageUrl = bookInfoResponse.getBookThumbnailImageUrl();
        this.bookViewCount = bookInfoResponse.getBookViewCount();
        this.bookDiscount = bookInfoResponse.getBookDiscount();
        this.bookStatus = bookInfoResponse.getBookStatus();
        this.publisher = bookInfoResponse.getPublisher();
        this.categories = bookInfoResponse.getCategories();
        this.tags = bookInfoResponse.getTags();
        this.authors = bookInfoResponse.getAuthors();
        this.likeCount = likeCount;
    }
}
