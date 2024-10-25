package com.nhnacademy.ssacthree_shop_api.bookset.book.domain;

import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.BookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.discount.domain.Discount;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "book")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookId;
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
    @Column(name = "book_isbn")
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

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    @NotNull
    private  Publisher publisher;

}
