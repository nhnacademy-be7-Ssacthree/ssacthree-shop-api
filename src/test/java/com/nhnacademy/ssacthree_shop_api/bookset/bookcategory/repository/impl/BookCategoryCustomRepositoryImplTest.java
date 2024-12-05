package com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.BookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.repository.BookCategoryCustomRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
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
class BookCategoryCustomRepositoryImplTest {

    private BookCategoryCustomRepository bookCategoryCustomRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp() {
        bookCategoryCustomRepository = new BookCategoryCustomRepositoryImpl(queryFactory);
    }

    @Test
    void testFindBookCategoriesByBookId_SingleCategory() {
        // Given
        // Create a category
        Category category = new Category();
        category.setCategoryName("Category One");
        category.setCategoryIsUsed(true);
        testEntityManager.persist(category);

        // Create a publisher
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Create a book
        Book book = createBook("Test Book", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // Create a BookCategory
        BookCategory bookCategory = new BookCategory(book, category);
        testEntityManager.persist(bookCategory);

        // Flush and clear the persistence context
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<Category> categories = bookCategoryCustomRepository.findBookCategoriesByBookId(
            book.getBookId());

        // Then

        assertThat(categories).hasSize(1);
        assertThat(categories.get(0).getCategoryName()).isEqualTo("Category One");
    }

    @Test
    void testFindBookCategoriesByBookId_MultipleCategories() {
        // Given
        // Create categories
        Category category1 = new Category();
        category1.setCategoryName("Category One");
        category1.setCategoryIsUsed(true);
        testEntityManager.persist(category1);

        Category category2 = new Category();
        category2.setCategoryName("Category Two");
        category2.setCategoryIsUsed(true);
        testEntityManager.persist(category2);

        // Create a publisher
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Create a book
        Book book = createBook("Test Book", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // Create BookCategories
        BookCategory bookCategory1 = new BookCategory(book, category1);
        testEntityManager.persist(bookCategory1);

        BookCategory bookCategory2 = new BookCategory(book, category2);
        testEntityManager.persist(bookCategory2);

        // Flush and clear the persistence context
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<Category> categories = bookCategoryCustomRepository.findBookCategoriesByBookId(
            book.getBookId());

        // Then

        assertThat(categories).hasSize(2);
        assertThat(categories)
            .extracting(Category::getCategoryName)
            .containsExactlyInAnyOrder("Category One", "Category Two");
    }

    @Test
    void testFindBookCategoriesByBookId_CategoryIsUsedFalse() {
        // Given
        // Create a category with categoryIsUsed = false
        Category category = new Category();
        category.setCategoryName("Unused Category");
        category.setCategoryIsUsed(false);
        testEntityManager.persist(category);

        // Create a publisher
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Create a book
        Book book = createBook("Test Book", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // Create a BookCategory
        BookCategory bookCategory = new BookCategory(book, category);
        testEntityManager.persist(bookCategory);

        // Flush and clear the persistence context
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<Category> categories = bookCategoryCustomRepository.findBookCategoriesByBookId(
            book.getBookId());

        // Then

        assertThat(categories).isEmpty();
    }

    @Test
    void testFindBookCategoriesByBookId_NoCategories() {
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
        List<Category> categories = bookCategoryCustomRepository.findBookCategoriesByBookId(
            book.getBookId());

        // Then

        assertThat(categories).isEmpty();
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
