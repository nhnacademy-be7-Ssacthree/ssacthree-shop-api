package com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.dto;

import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookAuthorDto {
    private Long bookId;
    private AuthorNameResponse authorNameResponse;
}
