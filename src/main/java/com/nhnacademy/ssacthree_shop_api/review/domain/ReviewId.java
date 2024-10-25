package com.nhnacademy.ssacthree_shop_api.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Embeddable
public class ReviewId implements Serializable {

    @Column(name="orders_id")
    private long orderId;

    @Column(name="book_id")
    private long bookId;

}
