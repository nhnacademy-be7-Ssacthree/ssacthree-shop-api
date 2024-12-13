package com.nhnacademy.ssacthree_shop_api.bookset.booktag.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.repository.BookTagCustomRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
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
class BookTagCustomRepositoryImplTest {

    private BookTagCustomRepository bookTagCustomRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp() {
        bookTagCustomRepository = new BookTagCustomRepositoryImpl(queryFactory);
    }

    @Test
    void testFindBookTagsByBookId_SingleTag() {
        // Given
        // Create a tag
        Tag tag = new Tag();
        tag.setTagName("Tag One");
        testEntityManager.persist(tag);

        // Create a publisher
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Create a book
        Book book = createBook("Test Book", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // Create a BookTag
        BookTag bookTag = new BookTag(book, tag);
        testEntityManager.persist(bookTag);

        // Flush and clear the persistence context
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<Tag> tags = bookTagCustomRepository.findBookTagsByBookId(book.getBookId());

        // Then

        assertThat(tags).hasSize(1);
        assertThat(tags.get(0).getTagName()).isEqualTo("Tag One");
    }

    @Test
    void testFindBookTagsByBookId_MultipleTags() {
        // Given
        // Create tags
        Tag tag1 = new Tag();
        tag1.setTagName("Tag One");
        testEntityManager.persist(tag1);

        Tag tag2 = new Tag();
        tag2.setTagName("Tag Two");
        testEntityManager.persist(tag2);

        // Create a publisher
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Create a book
        Book book = createBook("Test Book", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // Create BookTags
        BookTag bookTag1 = new BookTag(book, tag1);
        testEntityManager.persist(bookTag1);

        BookTag bookTag2 = new BookTag(book, tag2);
        testEntityManager.persist(bookTag2);

        // Flush and clear the persistence context
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<Tag> tags = bookTagCustomRepository.findBookTagsByBookId(book.getBookId());

        // Then
        
        assertThat(tags).hasSize(2);
        assertThat(tags)
            .extracting(Tag::getTagName)
            .containsExactlyInAnyOrder("Tag One", "Tag Two");
    }

    @Test
    void testFindBookTagsByBookId_NoTags() {
        // Given
        // Create a publisher
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Create a book
        Book book = createBook("Test Book", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // Flush and clear the persistence context
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<Tag> tags = bookTagCustomRepository.findBookTagsByBookId(book.getBookId());

        // Then
        assertThat(tags).isEmpty();
    }

    private Book createBook(String bookName, Publisher publisher, BookStatus status) {
        Book book = new Book();
        book.setBookName(bookName);
        book.setPublisher(publisher);
        book.setRegularPrice(10000);
        book.setSalePrice(8000);
        book.setBookThumbnailImageUrl("test_thumbnail_url");
        book.setBookViewCount(0);
        book.setBookDiscount(20);
        book.setIsPacked(true);
        book.setStock(10);
        book.setPublicationDate(LocalDateTime.now());
        book.setBookStatus(status);
        return book;
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
            return new JPAQueryFactory(entityManager);
        }
    }
}
