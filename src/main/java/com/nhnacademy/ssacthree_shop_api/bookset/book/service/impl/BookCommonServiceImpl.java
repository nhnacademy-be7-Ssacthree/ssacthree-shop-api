package com.nhnacademy.ssacthree_shop_api.bookset.book.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookCommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookCommonServiceImpl implements BookCommonService {

    @Override
    public BookInfoResponse findAllBooksExcludingDiscontinued() {
        return null;
    }

    @Override
    public BookInfoResponse getBook(Long bookId) {
        return null;
    }

}
