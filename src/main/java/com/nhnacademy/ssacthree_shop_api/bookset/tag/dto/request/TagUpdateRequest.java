package com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagUpdateRequest {
    private Long tagId;
    private String tagName;
}
