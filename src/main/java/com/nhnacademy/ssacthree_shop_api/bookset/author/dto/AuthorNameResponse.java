package com.nhnacademy.ssacthree_shop_api.bookset.author.dto;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorNameResponse {
    private Long authorId;
    private String authorName;

    public AuthorNameResponse(Author author){
        this.authorId = author.getAuthorId();
        this.authorName = author.getAuthorName();
    }
}
