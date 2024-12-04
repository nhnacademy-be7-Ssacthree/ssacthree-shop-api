package com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response;

import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
