package com.nhnacademy.ssacthree_shop_api.bookset.book.service;

import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookBaseResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BookMgmtService {

    //도서 전체 조회
    Page<BookSearchResponse> getAllBooks(Pageable pageable);

    // 도서 저장
    BookInfoResponse saveBook(BookSaveRequest bookSaveRequest);

    // 도서 수정
    BookInfoResponse updateBook(Long bookId, BookSaveRequest bookSaveRequest);

    // 도서 soft 삭제
    public BookInfoResponse deleteBook(Long bookId, BookSaveRequest bookSaveRequest);

    Page<BookInfoResponse> getBookById(Long bookId, Pageable pageable);


}
