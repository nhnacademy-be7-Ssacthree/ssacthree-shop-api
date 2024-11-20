package com.nhnacademy.ssacthree_shop_api.bookset.book.service;

import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookDeleteRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BookMgmtService {

    //도서 전체 조회
    Page<BookSearchResponse> getAllBooks(Pageable pageable);

    // 도서 저장
    BookInfoResponse saveBook(BookSaveRequest bookSaveRequest);

    // 도서 수정
    BookUpdateResponse updateBook(BookSaveRequest bookSaveRequest);

    // 도서 soft 삭제
    BookDeleteResponse deleteBook(Long bookId);

    BookInfoResponse getBookById(Long bookId);
}
