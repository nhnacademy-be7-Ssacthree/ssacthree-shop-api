package com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublisherDto {
    private long publisherId;
    private String publisherName;
    private boolean publisherIsUsed;
}
