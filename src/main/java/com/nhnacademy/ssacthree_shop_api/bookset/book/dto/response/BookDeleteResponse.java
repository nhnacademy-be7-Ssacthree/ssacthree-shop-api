package com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response;

import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class BookDeleteResponse {
    private Long bookId;
    private String bookName;
    private String bookInfo;
    private BookStatus bookStatus;
    private int stock;

    @Setter
    private List<AuthorNameResponse> authors;

    public BookDeleteResponse(Long bookId, String bookName, String bookInfo, BookStatus bookStatus,int stock) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookInfo = bookInfo;
        this.bookStatus = bookStatus;
        this.bookStatus = BookStatus.ON_SALE;
        this.stock = stock;
    }
}
