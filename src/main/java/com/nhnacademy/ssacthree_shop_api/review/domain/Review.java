package com.nhnacademy.ssacthree_shop_api.review.domain;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
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
    private LocalDateTime reviewDate = LocalDateTime.now();

    @Null
    private String reviewImageUrl;


}
