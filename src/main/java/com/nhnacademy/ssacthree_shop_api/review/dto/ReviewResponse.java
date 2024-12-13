package com.nhnacademy.ssacthree_shop_api.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewResponse {

    private int reviewRate;
    private String reviewTitle;
    private String reviewContent;
    private String reviewImage;

}
