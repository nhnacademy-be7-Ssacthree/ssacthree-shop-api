package com.nhnacademy.ssacthree_shop_api.bookset.discount.domain;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="discount_id")
    private long discountId;

    @NotNull
    private int bookDiscountLog;

    @NotNull
    private LocalDateTime bookDiscountDate;

    @ManyToOne
    @JoinColumn(name="bookId")
    @NotNull
    private Book book;
}
