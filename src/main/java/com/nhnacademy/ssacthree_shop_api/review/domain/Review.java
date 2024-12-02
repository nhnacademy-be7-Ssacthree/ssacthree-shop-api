package com.nhnacademy.ssacthree_shop_api.review.domain;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Review {

    @EmbeddedId
    private ReviewId reviewId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id", insertable = false, updatable = false) // orders_id 컬럼을 통해 Order 엔티티에 매핑
    private Order order; // Order 엔티티 필드 추가

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", insertable = false, updatable = false) // book_id 컬럼을 통해 Book 엔티티에 매핑
    private Book book; // Book 엔티티 필드 추가


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id")
    private Customer customer;

    @NotNull
    private int reviewRate;

    @NotNull
    @Size(min = 1,max = 50)
    private String reviewTitle;

    @NotNull
    @Size(min = 1,max = 1000)
    private String reviewContent;

    @NotNull
    private LocalDateTime reviewCreatedAt;

    private String reviewImageUrl;
}
