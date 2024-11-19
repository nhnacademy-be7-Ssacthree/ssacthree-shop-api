package com.nhnacademy.ssacthree_shop_api.bookset.author.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {
    private Long authorId;
    private String authorName;
    private String authorInfo;
}
