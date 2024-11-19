package com.nhnacademy.ssacthree_shop_api.bookset.author.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorGetResponse {
    private Long authorId;
    private String authorName;
    private String authorInfo;
}