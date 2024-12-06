package com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response;

import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.converter.BookStatusConverter;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateResponse {
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
    @Setter
    private PublisherNameResponse publisher;

    @Setter
    private List<CategoryNameResponse> categories;

    @Setter
    private List<TagInfoResponse> tags;

    @Setter
    private List<AuthorNameResponse> authors;

    public BookUpdateResponse(Book book) {
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
        if (book.getBookStatus() != null) {
            this.bookStatus = converter.convertToDatabaseColumn(book.getBookStatus());
        } else {
            // bookStatus가 null인 경우 처리
            this.bookStatus = BookStatus.ON_SALE.getStatus();  // 예시로 기본 상태값을 설정할 수 있습니다.
        }


        this.publisher = new PublisherNameResponse(book.getPublisher());

        // book의 bookCategory set을 stream() 메소드를 통해
        // bookCategory 요소 하나씩 CategoryNameResponse로 변환 후
        // collect를 통해 리스트로 묶어서 반환
        this.categories = book.getBookCategories().stream()
            .map(bookCategory -> new CategoryNameResponse(bookCategory.getCategory()))
            .toList();  // Stream.collect(Collectors.toList()) 대신 Stream.toList()

        this.tags = book.getBookTags().stream()
            .map(bookTag -> new TagInfoResponse(bookTag.getTag()))
            .toList();  // Stream.collect(Collectors.toList()) 대신 Stream.toList()

        this.authors = book.getBookAuthors().stream()
            .map(bookAuthor -> new AuthorNameResponse(bookAuthor.getAuthor()))
            .toList();  // Stream.collect(Collectors.toList()) 대신 Stream.toList()

    }

    public BookUpdateResponse(BookBaseResponse bookBaseResponse) {
        this.bookId = bookBaseResponse.getBookId();
        this.bookName = bookBaseResponse.getBookName();
        this.bookIndex = bookBaseResponse.getBookIndex();
        this.bookInfo = bookBaseResponse.getBookInfo();
        this.bookIsbn = bookBaseResponse.getBookIsbn();
        this.publicationDate = bookBaseResponse.getPublicationDate();
        this.regularPrice = bookBaseResponse.getRegularPrice();
        this.salePrice = bookBaseResponse.getSalePrice();
        this.isPacked = bookBaseResponse.isPacked();
        this.stock = bookBaseResponse.getStock();
        this.bookThumbnailImageUrl = bookBaseResponse.getBookThumbnailImageUrl();
        this.bookViewCount = bookBaseResponse.getBookViewCount();
        this.bookDiscount = bookBaseResponse.getBookDiscount();
        this.bookStatus = bookBaseResponse.getBookStatus();
        this.publisher = bookBaseResponse.getPublisher();
    }

}
