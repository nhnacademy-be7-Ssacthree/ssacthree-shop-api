package com.nhnacademy.ssacthree_shop_api.bookset.book.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    @Pattern(regexp = "^[가-힣 0-9a-zA-Z!@#$%^&*(),.?\":{}|<>]+$", message = "책 이름을 입력해주세요." )
    @Size(max = 255)
    private String bookName;
    @NotNull
    @Size(max = 255)
    private String bookIndex;
    @NotNull
    @Column(columnDefinition = "TEXT")
    private String bookInfo;
    @NotNull
    @Size(max = 20)
    private String isbn;
    @NotNull
    private LocalDateTime publicationDate;
    @NotNull
    @Pattern(regexp = "\\d+", message = "가격은 숫자로 입력해주세요")
    private int regularPrice;
    @NotNull
    @Pattern(regexp = "\\d+", message = "가격은 숫자로 입력해주세요")
    private int salePrice;
    @NotNull
    private boolean isPacked;
    @NotNull
    private int stock;
    @NotNull
    private String bookThumbnailImageUrl;
    @NotNull
    private int bookViewCount;
    private int bookDiscount;

}
