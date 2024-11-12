package com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublisherGetResponse {
    private Long publisherId;
    private String publisherName;
    private boolean publisherIsUsed;
}
