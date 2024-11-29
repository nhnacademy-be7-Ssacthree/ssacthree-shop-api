package com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringExclude;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookTag {
    @EmbeddedId
    private BookTagId bookTagId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    @ToStringExclude // to String 과 json back reference 어노테이션은 순환참조 문제 때문에 양방향의 경우 걸어줘야함.
    @JsonBackReference
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public BookTag(Book book, Tag tag) {
        this.book = book;
        this.tag = tag;
        this.bookTagId = new BookTagId(book.getBookId(), tag.getTagId());
    }
}
