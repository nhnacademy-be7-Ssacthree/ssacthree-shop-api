package com.nhnacademy.ssacthree_shop_api.bookset.book.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookCustomRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.BookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.domain.BookLike;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EntityScan(basePackages = "com.nhnacademy.ssacthree_shop_api")
@ActiveProfiles("test")
class BookCustomRepositoryImplTest {

    private BookCustomRepository bookCustomRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        bookCustomRepository = new BookCustomRepositoryImpl(queryFactory, categoryRepository);
    }

    @Test
    void testFindBooksByBookName() {
        // Given
        String bookName = "Test Book";

        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook(bookName, publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookListResponse> result = bookCustomRepository.findBooksByBookName(pageable, "Test");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        BookListResponse bookResponse = result.getContent().get(0);
        assertThat(bookResponse.getBookName()).isEqualTo(bookName);
    }

    @Test
    void testFindAllAvailableBooks() {
        // Given
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // 판매 중인 책
        Book book1 = createBook("Available Book 1", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book1);

        // 재고 없는 책
        Book book2 = createBook("Available Book 2", publisher, BookStatus.NO_STOCK);
        testEntityManager.persist(book2);

        // 판매 중단된 책
        Book book3 = createBook("Unavailable Book", publisher, BookStatus.DISCONTINUED);
        testEntityManager.persist(book3);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookListResponse> result = bookCustomRepository.findAllAvailableBooks(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
            .extracting(BookListResponse::getBookName)
            .containsExactlyInAnyOrder("Available Book 1", "Available Book 2");
    }

    @Test
    void testFindAllBooksByStatusNoStock() {
        // Given
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        Book book1 = createBook("No Stock Book", publisher, BookStatus.NO_STOCK);
        testEntityManager.persist(book1);

        Book book2 = createBook("On Sale Book", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book2);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookListResponse> result = bookCustomRepository.findAllBooksByStatusNoStock(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getBookName()).isEqualTo("No Stock Book");
    }

    @Test
    void testFindStatusDiscontinued() {
        // Given
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        Book book1 = createBook("Discontinued Book", publisher, BookStatus.DISCONTINUED);
        testEntityManager.persist(book1);

        Book book2 = createBook("On Sale Book", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book2);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookListResponse> result = bookCustomRepository.findStatusDiscontinued(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getBookName()).isEqualTo("Discontinued Book");
    }

    @Test
    void testFindBooksByAuthorId() {
        // Given
        // Author 생성 및 저장
        Author author = new Author("Author Name", "Author Info");
        testEntityManager.persist(author);

        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook("Book by Author", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // BookAuthor 생성 및 저장
        BookAuthor bookAuthor = new BookAuthor(book, author);
        testEntityManager.persist(bookAuthor);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookListResponse> result = bookCustomRepository.findBooksByAuthorId(
            author.getAuthorId(), pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        BookListResponse bookResponse = result.getContent().get(0);
        assertThat(bookResponse.getBookName()).isEqualTo("Book by Author");
    }

    @Test
    void testFindBooksByCategoryId() {
        // Given
        // Category 생성 및 저장
        Category category = new Category();
        category.setCategoryName("Test Category");
        category.setCategoryIsUsed(true);
        testEntityManager.persist(category);

        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook("Book in Category", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // BookCategory 생성 및 저장
        BookCategory bookCategory = new BookCategory(book, category);
        testEntityManager.persist(bookCategory);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookListResponse> result = bookCustomRepository.findBooksByCategoryId(
            category.getCategoryId(), pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        BookListResponse bookResponse = result.getContent().get(0);
        assertThat(bookResponse.getBookName()).isEqualTo("Book in Category");
    }

    @Test
    void testFindBooksByTagId() {
        // Given
        // Tag 생성 및 저장
        Tag tag = new Tag("Test Tag");
        testEntityManager.persist(tag);

        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook("Book with Tag", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // BookTag 생성 및 저장
        BookTag bookTag = new BookTag(book, tag);
        testEntityManager.persist(bookTag);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookListResponse> result = bookCustomRepository.findBooksByTagId(tag.getTagId(),
            pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        BookListResponse bookResponse = result.getContent().get(0);
        assertThat(bookResponse.getBookName()).isEqualTo("Book with Tag");
    }

    @Test
    void testFindBookLikesByCustomerId() {
        // Given
        // Customer 생성 및 저장
        Customer customer = new Customer("John Doe", "john@example.com", "010-1234-5678");
        testEntityManager.persist(customer);

        // Member 생성 및 저장
        Member member = new Member();
        setField(member, "customer", customer);
        testEntityManager.persist(member);

        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook("Liked Book", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // BookLike 생성 및 저장
        BookLike bookLike = new BookLike(book, member);
        testEntityManager.persist(bookLike);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookListResponse> result = bookCustomRepository.findBookLikesByCustomerId(
            member.getId(), pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        BookListResponse bookResponse = result.getContent().get(0);
        assertThat(bookResponse.getBookName()).isEqualTo("Liked Book");
    }

    @Test
    void testFindLikedBookIdByCustomerId() {
        // Given
        // Customer 생성 및 저장
        Customer customer = new Customer("John Doe", "john@example.com", "010-1234-5678");
        testEntityManager.persist(customer);

        // Member 생성 및 저장
        Member member = new Member();
        setField(member, "customer", customer);
        testEntityManager.persist(member);

        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook("Liked Book", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // BookLike 생성 및 저장
        BookLike bookLike = new BookLike(book, member);
        testEntityManager.persist(bookLike);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<Long> likedBookIds = bookCustomRepository.findLikedBookIdByCustomerId(member.getId());

        // Then
        assertThat(likedBookIds).containsExactly(book.getBookId());
    }

    @Test
    void testFindBookLikeByBookId() {
        // Given
        // Customer 생성 및 저장
        Customer customer = new Customer("John Doe", "john@example.com", "010-1234-5678");
        testEntityManager.persist(customer);

        // Member 생성 및 저장
        Member member = new Member();
        setField(member, "customer", customer);
        testEntityManager.persist(member);

        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook("Liked Book", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // BookLike 생성 및 저장
        BookLike bookLike = new BookLike(book, member);
        testEntityManager.persist(bookLike);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        Long likeCount = bookCustomRepository.findBookLikeByBookId(book.getBookId());

        // Then
        assertThat(likeCount).isEqualTo(1L);
    }


    @Test
    void testFindByBookIsbn() {
        // Given
        String isbn = "1234567890";

        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook("Test Book", publisher, BookStatus.ON_SALE);
        book.setBookIsbn(isbn);
        testEntityManager.persist(book);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        BookInfoResponse bookInfo = bookCustomRepository.findByBookIsbn(isbn);

        // Then
        assertThat(bookInfo).isNotNull();
        assertThat(bookInfo.getBookName()).isEqualTo("Test Book");
        assertThat(bookInfo.getBookIsbn()).isEqualTo(isbn);
    }

    @Test
    void testFindBookById() {
        // Given
        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook("Test Book", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        BookInfoResponse bookInfo = bookCustomRepository.findBookById(book.getBookId());

        // Then
        assertThat(bookInfo).isNotNull();
        assertThat(bookInfo.getBookName()).isEqualTo("Test Book");
    }

    @Test
    void testFindCategoriesByBookId() {
        // Given
        // Category 생성 및 저장
        Category category = new Category();
        category.setCategoryName("Test Category");
        category.setCategoryIsUsed(true);
        testEntityManager.persist(category);

        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook("Book in Category", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // BookCategory 생성 및 저장
        BookCategory bookCategory = new BookCategory(book, category);
        testEntityManager.persist(bookCategory);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<?> categories = bookCustomRepository.findCategoriesByBookId(book.getBookId());

        // Then
        assertThat(categories).hasSize(1);
    }

    @Test
    void testFindTagsByBookId() {
        // Given
        // Tag 생성 및 저장
        Tag tag = new Tag("Test Tag");
        testEntityManager.persist(tag);

        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook("Book with Tag", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // BookTag 생성 및 저장
        BookTag bookTag = new BookTag(book, tag);
        testEntityManager.persist(bookTag);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<?> tags = bookCustomRepository.findTagsByBookId(book.getBookId());

        // Then
        assertThat(tags).hasSize(1);
    }

    @Test
    void testFindAuthorsByBookId() {
        // Given
        // Author 생성 및 저장
        Author author = new Author("Author Name", "Author Info");
        testEntityManager.persist(author);

        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook("Book by Author", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // BookAuthor 생성 및 저장
        BookAuthor bookAuthor = new BookAuthor(book, author);
        testEntityManager.persist(bookAuthor);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<?> authors = bookCustomRepository.findAuthorsByBookId(book.getBookId());

        // Then
        assertThat(authors).hasSize(1);
    }

    @Test
    void testFindAuthorNamesByBookId() {
        // Given
        // Author 생성 및 저장
        Author author = new Author("Author Name", "Author Info");
        testEntityManager.persist(author);

        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook("Book by Author", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // BookAuthor 생성 및 저장
        BookAuthor bookAuthor = new BookAuthor(book, author);
        testEntityManager.persist(bookAuthor);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<String> authorNames = bookCustomRepository.findAuthorNamesByBookId(book.getBookId());

        // Then
        assertThat(authorNames).containsExactly("Author Name");
    }

    @Test
    void testFindPublisherNameByBookId() {
        // Given
        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook("Test Book", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        String publisherName = bookCustomRepository.findPublisherNameByBookId(book.getBookId());

        // Then
        assertThat(publisherName).isEqualTo("Test Publisher");
    }

    @Test
    void testFindTagNamesByBookId() {
        // Given
        // Tag 생성 및 저장
        Tag tag = new Tag("Test Tag");
        testEntityManager.persist(tag);

        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook("Book with Tag", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // BookTag 생성 및 저장
        BookTag bookTag = new BookTag(book, tag);
        testEntityManager.persist(bookTag);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<String> tagNames = bookCustomRepository.findTagNamesByBookId(book.getBookId());

        // Then
        assertThat(tagNames).containsExactly("Test Tag");
    }

    @Test
    void testFindCategoryNamesByBookId() {
        // Given
        // Category 생성 및 저장
        Category category = new Category();
        category.setCategoryName("Test Category");
        category.setCategoryIsUsed(true);
        testEntityManager.persist(category);

        // Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        testEntityManager.persist(publisher);

        // Book 생성 및 저장
        Book book = createBook("Book in Category", publisher, BookStatus.ON_SALE);
        testEntityManager.persist(book);

        // BookCategory 생성 및 저장
        BookCategory bookCategory = new BookCategory(book, category);
        testEntityManager.persist(bookCategory);

        // Flush 및 Clear
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<String> categoryNames = bookCustomRepository.findCategoryNamesByBookId(
            book.getBookId());

        // Then
        assertThat(categoryNames).containsExactly("Test Category");
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
        setField(book, "bookStatus", status);
        return book;
    }

    private void setField(Object targetObject, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = targetObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(targetObject, value);
        } catch (NoSuchFieldException e) {
            // 부모 클래스에서 필드를 찾음
            try {
                java.lang.reflect.Field field = targetObject.getClass().getSuperclass()
                    .getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(targetObject, value);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
            return new JPAQueryFactory(entityManager);
        }
    }
}
