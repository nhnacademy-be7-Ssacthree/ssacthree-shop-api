package com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringExclude;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_author_id")
    private Long bookAuthorId;

    @ManyToOne
    @JoinColumn(name="authorId")
    @NotNull
    private Author author;

    @ManyToOne
    @JoinColumn(name="bookId")
    @NotNull
    @ToStringExclude // to String 과 json back reference 어노테이션은 순환참조 문제 때문에 양방향의 경우 걸어줘야함.
    @JsonBackReference
    private Book book;

    public BookAuthor(Book book, Author author){
        this.book = book;
        this.author = author;
    }
}
