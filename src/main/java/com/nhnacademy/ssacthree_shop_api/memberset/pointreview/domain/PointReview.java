package com.nhnacademy.ssacthree_shop_api.memberset.pointreview.domain;


import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import com.nhnacademy.ssacthree_shop_api.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.review.domain.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name="point_review")
public class PointReview {

    @NotNull
    @Id
    private long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="point_history_id")
    private PointHistory pointHistory;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="order_id"),
            @JoinColumn(name="book_id")
    })
    private Review review;
}
