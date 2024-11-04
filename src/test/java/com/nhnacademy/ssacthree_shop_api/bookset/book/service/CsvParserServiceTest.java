package com.nhnacademy.ssacthree_shop_api.bookset.book.service;
import static org.mockito.Mockito.*;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.repository.AuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.repository.BookAuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.CsvParserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CsvParserServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookAuthorRepository bookAuthorRepository;

    @InjectMocks
    private CsvParserService csvParserService;

    @BeforeEach
    void setup() {
        // Additional setup can go here if needed
    }

//    @Test
//    void saveBooksFromCsv_ShouldSaveBooksAndAuthors() throws IOException {
//        // Arrange
//        String filePath = "D:/shoppingmall/csvfile/book_api_data.csv";
//
//        // Mock behaviors
//        Author author = new Author("한강", "");
//        when(authorRepository.findByAuthorName("한강")).thenReturn(Optional.empty());
//        when(authorRepository.save(any(Author.class))).thenReturn(author);
//
//        Publisher publisher = new Publisher("PublisherName");
//        when(publisherRepository.findByPublisherName(anyString())).thenReturn(Optional.of(publisher));
//        when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);
//
//        Book book = new Book(); // Adjust as needed
//        when(bookRepository.save(any(Book.class))).thenReturn(book);
//
//        // Act
//        csvParserService.saveBooksFromCsv(filePath);
//
//        // Assert
//        verify(bookRepository, atLeastOnce()).save(any(Book.class));
//        verify(authorRepository, atLeastOnce()).save(any(Author.class));
//        verify(publisherRepository, atLeastOnce()).save(any(Publisher.class));
//        verify(bookAuthorRepository, atLeastOnce()).save(any(BookAuthor.class));
//    }
}
