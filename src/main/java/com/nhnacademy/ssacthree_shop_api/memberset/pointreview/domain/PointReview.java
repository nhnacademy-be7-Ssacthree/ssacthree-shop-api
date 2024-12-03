package com.nhnacademy.ssacthree_shop_api.memberset.pointreview.domain;


import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
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


    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="point_history_id")
    private PointHistory pointHistory;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    @JoinColumn(name = "book_id")
    private Review review;
}
