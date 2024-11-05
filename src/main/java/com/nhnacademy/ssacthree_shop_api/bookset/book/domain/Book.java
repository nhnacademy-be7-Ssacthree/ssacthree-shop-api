package com.nhnacademy.ssacthree_shop_api.bookset.book.domain;


import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import jakarta.persistence.*; // jakarta -> javax?
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import lombok.Setter;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookId;
    private String bookName;
    private String bookIndex;
    private String bookInfo;
    private String bookIsbn;

    private LocalDateTime publicationDate;
//    @Pattern(regexp = "\\d+", message = "가격은 숫자로 입력해주세요")
    private int regularPrice;
//    @Pattern(regexp = "\\d+", message = "가격은 숫자로 입력해주세요")
    private int salePrice;
    private boolean isPacked;
    private int stock;
    private String bookThumbnailImageUrl;
    private int bookViewCount;
    private int bookDiscount;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private  Publisher publisher;

}
