package com.nhnacademy.ssacthree_shop_api.bookset.author.dto;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
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

    //책 전체 조회 시 필요한 생성자
    public AuthorNameResponse(String authorName){
        this.authorName = authorName;
    }
}
