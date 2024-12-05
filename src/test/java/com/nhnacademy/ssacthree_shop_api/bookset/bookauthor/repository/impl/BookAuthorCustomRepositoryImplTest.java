package com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.repository.BookAuthorCustomRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EntityScan(basePackages = "com.nhnacademy.ssacthree_shop_api")
@ActiveProfiles("test")
class BookAuthorCustomRepositoryImplTest {

    private BookAuthorCustomRepository bookAuthorCustomRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp() {
        bookAuthorCustomRepository = new BookAuthorCustomRepositoryImpl(queryFactory);
    }

    @Test
    void testFindBookAuthorByBookId() {
        // Given
        // Create an author
        Author author = new Author();
        author.setAuthorName("Author Name");
        author.setAuthorInfo("Author Info");
        testEntityManager.persist(author);

        // Create a publisher
        Publisher publisher = new Publisher("Publisher Name");
        testEntityManager.persist(publisher);

        // Create a book
        Book book = new Book();
        book.setBookName("Test Book");
        book.setPublisher(publisher);
        book.setRegularPrice(10000);
        book.setSalePrice(8000);
        book.setBookThumbnailImageUrl("test_thumbnail_url");
        book.setBookViewCount(0);
        book.setBookDiscount(20);
        book.setIsPacked(true);
        book.setStock(10);
        book.setPublicationDate(LocalDateTime.now());
        book.setBookStatus(BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // Create a BookAuthor
        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setAuthor(author);
        bookAuthor.setBook(book);
        testEntityManager.persist(bookAuthor);

        // Flush and clear the persistence context
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<Author> authors = bookAuthorCustomRepository.findBookAuthorByBookId(book.getBookId());

        // Then
        
        assertThat(authors).hasSize(1);
        Author foundAuthor = authors.get(0);
        assertThat(foundAuthor.getAuthorName()).isEqualTo("Author Name");
        assertThat(foundAuthor.getAuthorInfo()).isEqualTo("Author Info");
    }

    @Test
    void testFindBookAuthorByBookId_MultipleAuthors() {
        // Given
        // Create authors
        Author author1 = new Author();
        author1.setAuthorName("Author One");
        author1.setAuthorInfo("Author Info One");
        testEntityManager.persist(author1);

        Author author2 = new Author();
        author2.setAuthorName("Author Two");
        author2.setAuthorInfo("Author Info Two");
        testEntityManager.persist(author2);

        // Create a publisher
        Publisher publisher = new Publisher("Publisher Name");
        testEntityManager.persist(publisher);

        // Create a book
        Book book = new Book();
        book.setBookName("Test Book");
        book.setPublisher(publisher);
        book.setRegularPrice(10000);
        book.setSalePrice(8000);
        book.setBookThumbnailImageUrl("test_thumbnail_url");
        book.setBookViewCount(0);
        book.setBookDiscount(20);
        book.setIsPacked(true);
        book.setStock(10);
        book.setPublicationDate(LocalDateTime.now());
        book.setBookStatus(BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // Create BookAuthors
        BookAuthor bookAuthor1 = new BookAuthor();
        bookAuthor1.setAuthor(author1);
        bookAuthor1.setBook(book);
        testEntityManager.persist(bookAuthor1);

        BookAuthor bookAuthor2 = new BookAuthor();
        bookAuthor2.setAuthor(author2);
        bookAuthor2.setBook(book);
        testEntityManager.persist(bookAuthor2);

        // Flush and clear the persistence context
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<Author> authors = bookAuthorCustomRepository.findBookAuthorByBookId(book.getBookId());

        // Then

        assertThat(authors).hasSize(2);
        assertThat(authors)
            .extracting(Author::getAuthorName)
            .containsExactlyInAnyOrder("Author One", "Author Two");
    }

    @Test
    void testFindBookAuthorByBookId_NoAuthors() {
        // Given
        // Create a publisher
        Publisher publisher = new Publisher("Publisher Name");
        testEntityManager.persist(publisher);

        // Create a book
        Book book = new Book();
        book.setBookName("Test Book");
        book.setPublisher(publisher);
        book.setRegularPrice(10000);
        book.setSalePrice(8000);
        book.setBookThumbnailImageUrl("test_thumbnail_url");
        book.setBookViewCount(0);
        book.setBookDiscount(20);
        book.setIsPacked(true);
        book.setStock(10);
        book.setPublicationDate(LocalDateTime.now());
        book.setBookStatus(BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // Flush and clear the persistence context
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<Author> authors = bookAuthorCustomRepository.findBookAuthorByBookId(book.getBookId());

        // Then

        assertThat(authors).isEmpty();
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
            return new JPAQueryFactory(entityManager);
        }
    }
}
