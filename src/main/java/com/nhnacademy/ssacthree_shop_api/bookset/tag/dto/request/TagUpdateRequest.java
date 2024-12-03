package com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TagUpdateRequest {
    private Long tagId;
    private String tagName;
}
