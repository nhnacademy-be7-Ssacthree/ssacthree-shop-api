package com.nhnacademy.ssacthree_shop_api.bookset.book.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.mapper.BookMapper;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookMgmtService;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookMgmtServiceImpl implements BookMgmtService {


    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final PublisherRepository publisherRepository;

    /**
     * 새로운 도서를 저장합니다.
     * @param bookSaveRequest 저장할 도서 정보
     * @return 저장된 도서 정보
     */
    @Override
    public BookInfoResponse saveBook(BookSaveRequest bookSaveRequest) {
        Book book = bookMapper.bookSaveRequestToBook(bookSaveRequest);
        Publisher publisher = publisherRepository.findById(bookSaveRequest.getPublisherId()).orElseThrow(() -> new NotFoundException("해당 출판사가 존재하지 않습니다."));
        book.setPublisher(publisher);
        book.setBookStatus(BookStatus.ON_SALE);

        Book saveBook = bookRepository.save(book);

        //todo: 카테고리, 태그, 작가

        return new BookInfoResponse(saveBook);
    }

    @Override
    public BookInfoResponse updateBook(Long bookId, BookSaveRequest bookSaveRequest) {
        return null;
    }

    @Override
    public BookInfoResponse deleteBook(Long bookId) {
        return null;
    }

}
