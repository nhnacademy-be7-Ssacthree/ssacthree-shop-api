package com.nhnacademy.ssacthree_shop_api.bookset.book.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookSearchResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EntityScan(basePackages = "com.nhnacademy.ssacthree_shop_api")
@ActiveProfiles("test")
class BookMgmtRepositoryImplTest {

    private BookMgmtRepositoryImpl bookMgmtRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp() {
        bookMgmtRepository = new BookMgmtRepositoryImpl(queryFactory);
    }

    @Test
    void testFindAllBooks() {
        // Given
        Publisher publisher = new Publisher();
        publisher.setPublisherName("Test Publisher");
        testEntityManager.persist(publisher);

        Author author = new Author();
        testEntityManager.persist(author);

        Book book1 = new Book();
        book1.setBookName("Book 1");
        book1.setBookInfo("Info 1");
        book1.setBookStatus(BookStatus.ON_SALE);
        book1.setBookIsbn("1234567890");
        book1.setPublisher(publisher);
        testEntityManager.persist(book1);

        BookAuthor bookAuthor1 = new BookAuthor();
        bookAuthor1.setBook(book1);
        bookAuthor1.setAuthor(author);
        testEntityManager.persist(bookAuthor1);

        testEntityManager.flush();
        testEntityManager.clear();

        // When
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("bookName")));
        Page<BookSearchResponse> result = bookMgmtRepository.findAllBooks(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        BookSearchResponse bookResponse = result.getContent().get(0);
        assertThat(bookResponse.getBookName()).isEqualTo("Book 1");
        assertThat(bookResponse.getBookInfo()).isEqualTo("Info 1");
        assertThat(bookResponse.getAuthors()).hasSize(1);

        assertThat(bookResponse.getBookStatus()).isEqualTo(BookStatus.ON_SALE.getStatus());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
            return new JPAQueryFactory(entityManager);
        }
    }
}
