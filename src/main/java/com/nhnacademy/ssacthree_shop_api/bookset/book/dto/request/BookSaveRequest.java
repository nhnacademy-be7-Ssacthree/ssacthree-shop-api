package com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.converter.BookStatusConverter;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import jakarta.persistence.Convert;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BookSaveRequest {
    private String bookName;
    private String bookIndex; // 목차
    private String bookInfo; // 책 설명
    private String bookIsbn;
//    private LocalDateTime publicationDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;
    private int regularPrice; // 판매가
    private int salePrice; // 할인 가격
    private boolean isPacked;
    private int stock;
    private String bookThumbnailImageUrl;
    private int bookViewCount;
    private int bookDiscount; // 할인율

//    private String bookStatus; // 도서 상태

    // FK
    private Long publisherId;


    private List<Long> categoryIdList;

    private List<Long> authorIdList;

    private List<Long> tagIdList;

    public boolean getIsPacked() {
        return isPacked;
    }
}
