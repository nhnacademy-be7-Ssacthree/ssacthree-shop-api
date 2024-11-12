package com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto;

import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PublisherNameResponse {
    private Long publisherId;
    private String publisherName;

    public PublisherNameResponse(Publisher publisher) {
        this.publisherId = publisher.getPublisherId();
        this.publisherName = publisher.getPublisherName();
    }
}
