package com.nhnacademy.ssacthree_shop_api.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberReviewResponse {

    private Long orderId;
    private Long bookId;
    private String bookImageUrl;
    private String bookTitle;
    private int reviewRate;
    private String reviewTitle;
    private String reviewContent;
    private String reviewImageUrl;

}
