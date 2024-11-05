package com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookTag {
    @EmbeddedId
    private BookTagId bookTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public BookTag(Book book, Tag tag) {
        this.book = book;
        this.tag = tag;
        this.bookTagId = new BookTagId(book.getBookId(), tag.getTagId());
    }
}
