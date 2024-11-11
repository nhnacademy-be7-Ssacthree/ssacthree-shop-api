package com.nhnacademy.ssacthree_shop_api.bookset.booktag.dto;

import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookTagDto {
    private Long bookId;
    private TagInfoResponse tagInfoResponse;
}
