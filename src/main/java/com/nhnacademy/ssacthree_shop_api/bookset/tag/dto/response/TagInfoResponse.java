package com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response;

import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagInfoResponse {
    private Long tagId;
    private String tagName;

    public TagInfoResponse(Tag tag){
        this.tagId = tag.getTagId();
        this.tagName = tag.getTagName();
    }

    public TagInfoResponse(String tagName){
        this.tagName = tagName;
    }
}
