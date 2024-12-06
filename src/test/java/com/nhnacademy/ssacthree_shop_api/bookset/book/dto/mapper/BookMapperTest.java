package com.nhnacademy.ssacthree_shop_api.bookset.book.dto.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.author.repository.AuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.BookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.repository.TagRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookMapperTest {

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private BookMapper bookMapper;

    @Test
    public void testBookSaveRequestToBook() {
        // Prepare test data
        BookSaveRequest bookSaveRequest = new BookSaveRequest();
        bookSaveRequest.setBookName("Test Book");
        bookSaveRequest.setBookIndex("12345");
        bookSaveRequest.setBookInfo("This is a test book");
        bookSaveRequest.setBookIsbn("ISBN-12345");
        bookSaveRequest.setPublicationDate(LocalDate.of(2023, 10, 1));
        bookSaveRequest.setRegularPrice(10000);
        bookSaveRequest.setSalePrice(9000);
        bookSaveRequest.setPacked(false);
        bookSaveRequest.setStock(100);
        bookSaveRequest.setBookThumbnailImageUrl("http://example.com/image.jpg");
        bookSaveRequest.setBookViewCount(0);
        bookSaveRequest.setBookDiscount(10);
        bookSaveRequest.setPublisherId(1L);
        bookSaveRequest.setCategoryIdList(Arrays.asList(1L, 2L));
        bookSaveRequest.setAuthorIdList(Arrays.asList(1L, 2L));
        bookSaveRequest.setTagIdList(Arrays.asList(1L, 2L));

        // Mocking repository methods
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);
        publisher.setPublisherName("Test Publisher");
        when(publisherRepository.findById(1L)).thenReturn(java.util.Optional.of(publisher));

        Category category1 = new Category();
        category1.setCategoryId(1L);
        category1.setCategoryName("Category 1");

        Category category2 = new Category();
        category2.setCategoryId(2L);
        category2.setCategoryName("Category 2");

        when(categoryRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(
            Arrays.asList(category1, category2));

        Author author1 = new Author();
        author1.setAuthorId(1L);
        author1.setAuthorName("Author 1");

        Author author2 = new Author();
        author2.setAuthorId(2L);
        author2.setAuthorName("Author 2");

        when(authorRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(
            Arrays.asList(author1, author2));

        Tag tag1 = new Tag();
        tag1.setTagId(1L);
        tag1.setTagName("Tag 1");

        Tag tag2 = new Tag();
        tag2.setTagId(2L);
        tag2.setTagName("Tag 2");

        when(tagRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(
            Arrays.asList(tag1, tag2));

        // Call the method under test
        Book book = bookMapper.bookSaveRequestToBook(bookSaveRequest);

        // Assertions
        assertNotNull(book);
        assertEquals("Test Book", book.getBookName());
        assertEquals("12345", book.getBookIndex());
        assertEquals("This is a test book", book.getBookInfo());
        assertEquals("ISBN-12345", book.getBookIsbn());
        assertEquals(LocalDateTime.of(2023, 10, 1, 0, 0), book.getPublicationDate());
        assertEquals(10000, book.getRegularPrice());
        assertEquals(9000, book.getSalePrice());
        assertFalse(book.getIsPacked());
        assertEquals(100, book.getStock());
        assertEquals("http://example.com/image.jpg", book.getBookThumbnailImageUrl());
        assertEquals(0, book.getBookViewCount());
        assertEquals(10, book.getBookDiscount());
        assertEquals(BookStatus.ON_SALE, book.getBookStatus());
        assertEquals(publisher, book.getPublisher());

        // Verify categories
        assertNotNull(book.getBookCategories());
        assertEquals(2, book.getBookCategories().size());
        List<Category> categories = book.getBookCategories().stream()
            .map(BookCategory::getCategory)
            .toList();
        assertTrue(categories.contains(category1));
        assertTrue(categories.contains(category2));

        // Verify authors
        assertNotNull(book.getBookAuthors());
        assertEquals(2, book.getBookAuthors().size());
        List<Author> authors = book.getBookAuthors().stream()
            .map(BookAuthor::getAuthor)
            .toList();
        assertTrue(authors.contains(author1));
        assertTrue(authors.contains(author2));

        // Verify tags
        assertNotNull(book.getBookTags());
        assertEquals(2, book.getBookTags().size());
        List<Tag> tags = book.getBookTags().stream()
            .map(BookTag::getTag)
            .toList();
        assertTrue(tags.contains(tag1));
        assertTrue(tags.contains(tag2));
    }

    @Test
    public void testBookToBookInfoResponse() {
        // Prepare test data
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);
        publisher.setPublisherName("Test Publisher");

        Book book = new Book();
        book.setBookId(1L);
        book.setBookName("Test Book");
        book.setBookIndex("12345");
        book.setBookInfo("This is a test book");
        book.setBookIsbn("ISBN-12345");
        book.setPublicationDate(LocalDateTime.of(2023, 10, 1, 0, 0));
        book.setRegularPrice(10000);
        book.setSalePrice(9000);
        book.setIsPacked(false);
        book.setStock(100);
        book.setBookThumbnailImageUrl("http://example.com/image.jpg");
        book.setBookViewCount(0);
        book.setBookDiscount(10);
        book.setBookStatus(BookStatus.ON_SALE);
        book.setPublisher(publisher);

        Category category1 = new Category();
        category1.setCategoryId(1L);
        category1.setCategoryName("Category 1");

        Category category2 = new Category();
        category2.setCategoryId(2L);
        category2.setCategoryName("Category 2");

        BookCategory bookCategory1 = new BookCategory(book, category1);
        BookCategory bookCategory2 = new BookCategory(book, category2);
        book.addCategory(bookCategory1);
        book.addCategory(bookCategory2);

        Author author1 = new Author();
        author1.setAuthorId(1L);
        author1.setAuthorName("Author 1");

        Author author2 = new Author();
        author2.setAuthorId(2L);
        author2.setAuthorName("Author 2");

        BookAuthor bookAuthor1 = new BookAuthor(book, author1);
        BookAuthor bookAuthor2 = new BookAuthor(book, author2);
        book.addAuthor(bookAuthor1);
        book.addAuthor(bookAuthor2);

        Tag tag1 = new Tag();
        tag1.setTagId(1L);
        tag1.setTagName("Tag 1");

        Tag tag2 = new Tag();
        tag2.setTagId(2L);
        tag2.setTagName("Tag 2");

        BookTag bookTag1 = new BookTag(book, tag1);
        BookTag bookTag2 = new BookTag(book, tag2);
        book.addTag(bookTag1);
        book.addTag(bookTag2);

        // Call the method under test
        BookInfoResponse response = bookMapper.bookToBookInfoResponse(book);

        // Assertions
        assertNotNull(response);
        assertEquals(1L, response.getBookId());
        assertEquals("Test Book", response.getBookName());
        assertEquals("12345", response.getBookIndex());
        assertEquals("This is a test book", response.getBookInfo());
        assertEquals("ISBN-12345", response.getBookIsbn());
        assertEquals(LocalDateTime.of(2023, 10, 1, 0, 0), response.getPublicationDate());
        assertEquals(10000, response.getRegularPrice());
        assertEquals(9000, response.getSalePrice());
        assertFalse(response.isPacked());
        assertEquals(100, response.getStock());
        assertEquals("http://example.com/image.jpg", response.getBookThumbnailImageUrl());
        assertEquals(0, response.getBookViewCount());
        assertEquals(10, response.getBookDiscount());
        assertEquals("ON_SALE", response.getBookStatus());

        // Verify publisher
        assertNotNull(response.getPublisher());
        assertEquals(1L, response.getPublisher().getPublisherId());
        assertEquals("Test Publisher", response.getPublisher().getPublisherName());

        // Verify categories
        assertNotNull(response.getCategories());
        assertEquals(2, response.getCategories().size());
        List<Long> categoryIds = response.getCategories().stream()
            .map(CategoryNameResponse::getCategoryId)
            .toList();
        assertTrue(categoryIds.contains(1L));
        assertTrue(categoryIds.contains(2L));

        // Verify authors
        assertNotNull(response.getAuthors());
        assertEquals(2, response.getAuthors().size());
        List<Long> authorIds = response.getAuthors().stream()
            .map(AuthorNameResponse::getAuthorId)
            .toList();
        assertTrue(authorIds.contains(1L));
        assertTrue(authorIds.contains(2L));

        // Verify tags
        assertNotNull(response.getTags());
        assertEquals(2, response.getTags().size());
        List<Long> tagIds = response.getTags().stream()
            .map(TagInfoResponse::getTagId)
            .toList();
        assertTrue(tagIds.contains(1L));
        assertTrue(tagIds.contains(2L));
    }
}
