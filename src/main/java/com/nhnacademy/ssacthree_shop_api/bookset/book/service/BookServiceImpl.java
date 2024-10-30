package com.nhnacademy.ssacthree_shop_api.bookset.book.service;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.BookDto;
import com.nhnacademy.ssacthree_shop_api.bookset.book.mapper.BookMapper;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }
    @Override
    public void registerBook(BookDto bookDto) {
        Book book = bookMapper.convertToBook(bookDto);
        bookRepository.save(book);
    }
}


