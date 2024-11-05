package com.nhnacademy.ssacthree_shop_api.bookset.author.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorCreateRequest {
    @NotNull
    private String authorName;
    @NotNull
    private String authorInfo;
}
