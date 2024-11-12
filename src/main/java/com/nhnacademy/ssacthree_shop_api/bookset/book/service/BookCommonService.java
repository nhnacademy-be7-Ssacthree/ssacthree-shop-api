package com.nhnacademy.ssacthree_shop_api.bookset.book.service;

import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookCommonService {
    // 도서 전체 조회(판매 중, 재고 없음)
    Page<BookInfoResponse> getAllAvailableBooks(Pageable pageable);

    // 도서 아이디 검색
    BookInfoResponse getBook(Long bookId);

    // 도서 최신순 검색
    Page<BookInfoResponse> getRecentBooks(Pageable pageable);

    // 도서 이름 검색
    Page<BookInfoResponse> getBooksByBookName(Pageable pageable, String bookName);

    // 도서 ISBN으로 도서 검색
    BookInfoResponse getBooksByBookIsbn(String isbn);

    Page<BookInfoResponse> getBooksByAuthorId(Pageable pageable, Long authorId);
}
