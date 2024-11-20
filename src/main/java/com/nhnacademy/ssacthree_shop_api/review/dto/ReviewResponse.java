package com.nhnacademy.ssacthree_shop_api.review.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewResponse {

    private String memberId;
    private int reviewRate;
    private String reviewTitle;
    private String reviewContent;
    private LocalDateTime reviewCreatedAt;
    private String reviewImage;

}
