package com.nhnacademy.ssacthree_shop_api.bookset.book.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookBaseResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookMgmtRepository {
    Page<BookSearchResponse> findAllBooks(Pageable pageable);

    Page<BookInfoResponse> findBooksByBookId(Long bookId, Pageable pageable);

    List<BookInfoResponse> findBookByBookIsbnForAdmin(String bookIsbn);
}
