package com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookCategory {
    @EmbeddedId
    private BookCategoryId bookCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    @ToStringExclude // to String 과 json back reference 어노테이션은 순환참조 문제 때문에 양방향의 경우 걸어줘야함.
    @JsonBackReference
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    public BookCategory(Book book, Category category){
        this.book = book;
        this.category = category;
        this.bookCategoryId = new BookCategoryId(book.getBookId(), category.getCategoryId());
    }
}
