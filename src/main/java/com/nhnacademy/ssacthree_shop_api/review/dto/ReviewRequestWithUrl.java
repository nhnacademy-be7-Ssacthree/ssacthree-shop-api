package com.nhnacademy.ssacthree_shop_api.review.dto;

import lombok.Getter;

@Getter
public class ReviewRequestWithUrl {

    private int reviewRate;
    private String reviewTitle;
    private String reviewContent;
    private String reviewImageUrl;

}