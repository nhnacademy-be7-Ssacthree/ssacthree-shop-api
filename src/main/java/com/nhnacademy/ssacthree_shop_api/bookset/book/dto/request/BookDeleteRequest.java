package com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request;

import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class BookDeleteRequest {
    private Long bookId;
    private String bookName;
    private String bookInfo;
    private BookStatus bookStatus;

    @Setter
    private List<AuthorNameResponse> authors;

    public BookDeleteRequest(Long bookId, String bookName, String bookInfo) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookInfo = bookInfo;
        this.bookStatus = BookStatus.ON_SALE;
    }

}