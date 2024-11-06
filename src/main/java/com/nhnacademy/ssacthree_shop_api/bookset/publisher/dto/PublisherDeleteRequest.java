package com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PublisherDeleteRequest {

    @NotNull
    private Long publisherId;
}
