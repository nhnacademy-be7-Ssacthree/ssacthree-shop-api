package com.nhnacademy.ssacthree_shop_api.bookset.book.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookCommonService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookCommonServiceImpl implements BookCommonService {
    private final BookRepository bookRepository;

    @Override
    public Page<BookInfoResponse> getAllAvailableBooks(Pageable pageable) {
        return bookRepository.findAllAvailableBooks(pageable);
    }

    @Override
    public BookInfoResponse getBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("해당 도서가 존재하지 않습니다."));
        return null;
    }

    @Override
    public Page<BookInfoResponse> getRecentBooks(Pageable pageable) {
        return bookRepository.findRecentBooks(pageable);
    }

}
