package com.nhnacademy.ssacthree_shop_api.bookset.book.service;

import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;

public interface BookCommonService {
    // 도서 전체 조회(판매 중단 제외)
    BookInfoResponse findAllBooksExcludingDiscontinued();

    // 도서 아이디 검색
    BookInfoResponse getBook(Long bookId);
}
