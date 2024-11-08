package com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagGetResponse {

    private Long tagId;
    private String tagName;

}
