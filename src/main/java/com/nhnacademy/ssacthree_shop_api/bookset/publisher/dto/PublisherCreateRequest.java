package com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PublisherCreateRequest {

    @NotBlank
    private String publisherName;
}
