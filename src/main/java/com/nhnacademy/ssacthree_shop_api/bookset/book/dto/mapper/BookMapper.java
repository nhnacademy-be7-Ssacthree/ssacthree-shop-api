package com.nhnacademy.ssacthree_shop_api.bookset.book.dto.mapper;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.converter.BookStatusConverter;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherRepository;

import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Component;


@Component
public class BookMapper {
    public Book bookSaveRequestToBook(BookSaveRequest bookSaveRequest) {
        BookStatusConverter converter = new BookStatusConverter();

        Book book = new Book();
        book.setBookName(bookSaveRequest.getBookName());
        book.setBookIndex(bookSaveRequest.getBookIndex());
        book.setBookInfo(bookSaveRequest.getBookInfo());
        book.setBookIsbn(bookSaveRequest.getBookIsbn());
        book.setPublicationDate(bookSaveRequest.getPublicationDate());
        book.setRegularPrice(bookSaveRequest.getRegularPrice());
        book.setSalePrice(bookSaveRequest.getSalePrice());
        book.setIsPacked(bookSaveRequest.getIsPacked());
        book.setStock(bookSaveRequest.getStock());
        book.setBookThumbnailImageUrl(bookSaveRequest.getBookThumbnailImageUrl());
        book.setBookViewCount(bookSaveRequest.getBookViewCount());
        book.setBookDiscount(bookSaveRequest.getBookDiscount());
        return book;
    }

}
