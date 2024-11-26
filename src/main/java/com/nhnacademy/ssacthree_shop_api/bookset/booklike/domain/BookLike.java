package com.nhnacademy.ssacthree_shop_api.bookset.booklike.domain;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookLike {
    @EmbeddedId
    private BookLikeId bookLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerId")
    @JoinColumn(name = "customer_id")
    private Member member;

    public BookLike(Book book, Member member) {
        this.book = book;
        this.member = member;
        this.bookLikeId = new BookLikeId(book.getBookId(), member.getCustomer().getCustomerId());
    }

}
