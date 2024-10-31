package com.nhnacademy.ssacthree_shop_api.bookset.author.dto;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {
    private long authorId;
    private String authorName;
    private String authorInfo;

    public AuthorDto(String authorName, String authorInfo) {
        this.authorName = authorName;
        this.authorInfo = authorInfo;
    }

    public Author convertToAuthorEntity() {
        return new Author(
                0,
                this.authorName,
                this.authorInfo);

    }


}
