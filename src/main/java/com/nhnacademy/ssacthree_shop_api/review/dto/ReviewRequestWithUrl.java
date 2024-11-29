package com.nhnacademy.ssacthree_shop_api.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewRequestWithUrl {

    private int reviewRate;
    private String reviewTitle;
    private String reviewContent;
    private String reviewImageUrl;

}
