package com.nhnacademy.ssacthree_shop_api.bookset.book.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BookMgmtRepository {
    Page<BookSearchResponse> findAllBooks(Pageable pageable);


}
