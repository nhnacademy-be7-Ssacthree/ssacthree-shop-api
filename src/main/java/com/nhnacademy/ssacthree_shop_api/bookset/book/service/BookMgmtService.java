package com.nhnacademy.ssacthree_shop_api.bookset.book.service;

import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;

public interface BookMgmtService {
    // 도서 저장
    BookInfoResponse saveBook(BookSaveRequest bookSaveRequest);

    // 도서 수정
    BookInfoResponse updateBook(Long bookId, BookSaveRequest bookSaveRequest);

    // 도서 soft 삭제
    BookInfoResponse deleteBook(Long bookId);


}
