package com.nhnacademy.ssacthree_shop_api.couponset.bookcoupon.domain;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookCoupon {
    @Id
    @ManyToOne
    @JoinColumn(name="couponId")
    private Coupon coupon;

    @NotNull
    @ManyToOne
    @JoinColumn(name="bookId")
    private Book book;
}
