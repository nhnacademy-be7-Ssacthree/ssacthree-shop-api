package com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response;

import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class BookSearchResponse {
    private Long bookId;
    private String bookName;
    private String bookInfo;

    @Setter
    private List<AuthorNameResponse> authors;

    public BookSearchResponse(Long bookId, String bookName, String bookInfo) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookInfo = bookInfo;
    }

}
