package com.nhnacademy.ssacthree_shop_api.bookset.book.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.author.repository.AuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookDeleteResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookUpdateResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.BookAlreadyExistsException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.BookNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.mapper.BookMapper;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookSearchResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.repository.TagRepository;

import java.util.List;
import java.util.Optional;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

class BookMgmtServiceImplTest {

    private static final String ON_SALE = "판매 중";
    private static final String BOOK_ID_NOT_FOUND_MESSAGE = "해당 책 아이디를 찾을 수 없습니다.";
    @Mock
    private BookRepository bookRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookMapper bookMapper;


    @InjectMocks
    private BookMgmtServiceImpl bookMgmtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBooks_shouldReturnPagedBookSearchResponseWithAuthors() {
        // Given: 페이징 요청 및 Mock 데이터 설정
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bookName"));

        BookSearchResponse book1 = new BookSearchResponse(1L, "Book 1", "Info 1", ON_SALE);
        BookSearchResponse book2 = new BookSearchResponse(2L, "Book 2", "Info 2", ON_SALE);

        List<AuthorNameResponse> authorsForBook1 = List.of(
            new AuthorNameResponse("Author 1"),
            new AuthorNameResponse("Author 2")
        );

        List<AuthorNameResponse> authorsForBook2 = List.of(
            new AuthorNameResponse("Author 3"),
            new AuthorNameResponse("Author 4")
        );

        Page<BookSearchResponse> mockPage = new PageImpl<>(List.of(book1, book2), pageable, 2);

        // Mock Repository 동작 설정
        when(bookRepository.findAllBooks(pageable)).thenReturn(mockPage);
        when(bookRepository.findAuthorsByBookId(1L)).thenReturn(authorsForBook1);
        when(bookRepository.findAuthorsByBookId(2L)).thenReturn(authorsForBook2);

        // When: 서비스 메서드 호출
        Page<BookSearchResponse> result = bookMgmtService.getAllBooks(pageable);

        // Then: 결과 검증
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());

        BookSearchResponse resultBook1 = result.getContent().get(0);
        assertEquals(1L, resultBook1.getBookId());
        assertEquals("Book 1", resultBook1.getBookName());
        assertEquals(2, resultBook1.getAuthors().size());
        assertEquals("Author 1", resultBook1.getAuthors().get(0).getAuthorName());
        assertEquals("Author 2", resultBook1.getAuthors().get(1).getAuthorName());

        BookSearchResponse resultBook2 = result.getContent().get(1);
        assertEquals(2L, resultBook2.getBookId());
        assertEquals("Book 2", resultBook2.getBookName());
        assertEquals(2, resultBook2.getAuthors().size());
        assertEquals("Author 3", resultBook2.getAuthors().get(0).getAuthorName());
        assertEquals("Author 4", resultBook2.getAuthors().get(1).getAuthorName());

        // Repository 호출 검증
        verify(bookRepository, times(1)).findAllBooks(pageable);
        verify(bookRepository, times(1)).findAuthorsByBookId(1L);
        verify(bookRepository, times(1)).findAuthorsByBookId(2L);
    }

    @Test
    void saveBook_shouldSaveAndReturnBookInfoResponse() {
        // Given: 요청 데이터 및 Mock 설정
        BookSaveRequest bookSaveRequest = new BookSaveRequest();
        bookSaveRequest.setBookName("Test Book");
        bookSaveRequest.setBookIsbn("1234567890123");
        bookSaveRequest.setPublisherId(1L);
        bookSaveRequest.setCategoryIdList(List.of(1L, 2L));
        bookSaveRequest.setTagIdList(List.of(1L, 2L));
        bookSaveRequest.setAuthorIdList(List.of(1L, 2L)); // 작가 ID 리스트

        Book mockBook = new Book();
        mockBook.setBookName("Test Book");
        mockBook.setBookIsbn("1234567890123");

        Publisher mockPublisher = new Publisher();
        mockPublisher.setPublisherId(1L);
        mockPublisher.setPublisherName("Test Publisher");

        Category mockCategory1 = new Category();
        mockCategory1.setCategoryId(1L);
        mockCategory1.setCategoryName("Category 1");

        Category mockCategory2 = new Category();
        mockCategory2.setCategoryId(2L);
        mockCategory2.setCategoryName("Category 2");

        Tag mockTag1 = new Tag();
        mockTag1.setTagId(1L);
        mockTag1.setTagName("Tag 1");

        Tag mockTag2 = new Tag();
        mockTag2.setTagId(2L);
        mockTag2.setTagName("Tag 2");

        Author mockAuthor1 = new Author();
        mockAuthor1.setAuthorId(1L);
        mockAuthor1.setAuthorName("Author 1");

        Author mockAuthor2 = new Author();
        mockAuthor2.setAuthorId(2L);
        mockAuthor2.setAuthorName("Author 2");

        when(bookMapper.bookSaveRequestToBook(bookSaveRequest)).thenReturn(mockBook);
        when(bookRepository.findBookByBookIsbn("1234567890123")).thenReturn(Optional.empty());
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(mockPublisher));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory1));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(mockCategory2));
        when(tagRepository.findById(1L)).thenReturn(Optional.of(mockTag1));
        when(tagRepository.findById(2L)).thenReturn(Optional.of(mockTag2));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(mockAuthor1)); // 작가 ID 1 Mock 설정
        when(authorRepository.findById(2L)).thenReturn(Optional.of(mockAuthor2)); // 작가 ID 2 Mock 설정
        when(bookRepository.save(mockBook)).thenReturn(mockBook);

        // When: 서비스 메서드 호출
        BookInfoResponse response = bookMgmtService.saveBook(bookSaveRequest);

        // Then: 결과 검증
        assertNotNull(response);
        assertEquals("Test Book", response.getBookName());
        assertEquals("1234567890123", response.getBookIsbn());
        verify(bookRepository, times(1)).save(mockBook);
    }

    @Test
    void saveBook_shouldThrowExceptionIfBookAlreadyExists() {
        // Given: Mock 데이터 설정
        BookSaveRequest bookSaveRequest = new BookSaveRequest();
        bookSaveRequest.setBookName("Test Book");
        bookSaveRequest.setBookIsbn("1234567890123");

        Book existingBook = new Book();
        existingBook.setBookId(1L);
        existingBook.setBookName("Existing Book");
        existingBook.setBookIsbn("1234567890123");

        // Mock 설정
        when(bookMapper.bookSaveRequestToBook(bookSaveRequest)).thenReturn(existingBook);
        when(bookRepository.findBookByBookIsbn("1234567890123"))
            .thenReturn(Optional.of(existingBook));

        // When & Then: 중복 ISBN에 대해 예외 검증
        BookAlreadyExistsException exception = assertThrows(BookAlreadyExistsException.class,
            () -> bookMgmtService.saveBook(bookSaveRequest));

        // 예외 메시지 검증
        String expectedMessage = "이미 존재하는 책입니다. 등록하실 수 없습니다. (ISBN: 1234567890123)";
        assertEquals(expectedMessage, exception.getMessage());

        // `bookRepository.save`가 호출되지 않았는지 확인
        verify(bookRepository, never()).save(any());
    }

    @Test
    void saveBook_shouldUpdateCategoriesTagsAuthorsCorrectly() {
        // Given: Mock 데이터 설정
        BookSaveRequest bookSaveRequest = new BookSaveRequest();
        bookSaveRequest.setBookName("Test Book");
        bookSaveRequest.setBookIsbn("1234567890123");
        bookSaveRequest.setCategoryIdList(List.of(1L, 2L));
        bookSaveRequest.setTagIdList(List.of(1L, 2L));
        bookSaveRequest.setAuthorIdList(List.of(1L, 2L));
        bookSaveRequest.setPublisherId(1L);

        Book existingBook = new Book();
        existingBook.setBookCategories(new HashSet<>());
        existingBook.setBookTags(new HashSet<>());
        existingBook.setBookAuthors(new HashSet<>());

        // Mock Category, Tag, and Author
        Category category1 = new Category();
        category1.setCategoryId(1L);
        Category category2 = new Category();
        category2.setCategoryId(2L);

        Tag tag1 = new Tag();
        tag1.setTagId(1L);
        Tag tag2 = new Tag();
        tag2.setTagId(2L);

        Author author1 = new Author();
        author1.setAuthorId(1L);
        Author author2 = new Author();
        author2.setAuthorId(2L);

        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);

        // Mock 설정
        when(bookMapper.bookSaveRequestToBook(bookSaveRequest)).thenReturn(existingBook);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category2));
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag1));
        when(tagRepository.findById(2L)).thenReturn(Optional.of(tag2));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author1));
        when(authorRepository.findById(2L)).thenReturn(Optional.of(author2));
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(bookRepository.findBookByBookIsbn("1234567890123")).thenReturn(Optional.empty());

        // When: 메서드 실행
        bookMgmtService.saveBook(bookSaveRequest);

        // Then: 카테고리, 태그, 작가 업데이트 검증
        assertEquals(2, existingBook.getBookCategories().size());
        assertEquals(2, existingBook.getBookTags().size());
        assertEquals(2, existingBook.getBookAuthors().size());

        // `bookRepository.save` 호출 확인
        verify(bookRepository).save(existingBook);
    }

    @Test
    void updateBook_shouldUpdateBookDetailsSuccessfully() {
        // Given: Mock 데이터 설정
        BookUpdateRequest bookUpdateRequest = new BookUpdateRequest();
        bookUpdateRequest.setBookId(1L);
        bookUpdateRequest.setBookName("Updated Book Name");
        bookUpdateRequest.setBookIndex("Updated Index");
        bookUpdateRequest.setBookInfo("Updated Info");
        bookUpdateRequest.setBookIsbn("1234567890123");
        bookUpdateRequest.setRegularPrice(20000);
        bookUpdateRequest.setSalePrice(18000);
        bookUpdateRequest.setPacked(true);
        bookUpdateRequest.setStock(100);
        bookUpdateRequest.setBookThumbnailImageUrl("https://example.com/image.jpg");
        bookUpdateRequest.setBookViewCount(500);
        bookUpdateRequest.setBookDiscount(10);
        bookUpdateRequest.setBookStatus(BookStatus.ON_SALE.name()); // 유효한 상태값
        bookUpdateRequest.setPublisherId(1L);
        bookUpdateRequest.setCategoryIdList(List.of(1L, 2L));
        bookUpdateRequest.setTagIdList(List.of(1L, 2L));
        bookUpdateRequest.setAuthorIdList(List.of(1L, 2L));

        Book existingBook = new Book();
        existingBook.setBookId(1L);
        existingBook.setBookName("Old Book Name");
        existingBook.setBookIndex("Old Index");
        existingBook.setBookInfo("Old Info");
        existingBook.setBookIsbn("1234567890123");
        existingBook.setRegularPrice(15000);
        existingBook.setSalePrice(12000);
        existingBook.setIsPacked(false);
        existingBook.setStock(50);
        existingBook.setBookThumbnailImageUrl("https://example.com/old_image.jpg");
        existingBook.setBookViewCount(100);
        existingBook.setBookDiscount(5);
        existingBook.setBookStatus(BookStatus.DELETE_BOOK);
        existingBook.setBookCategories(new HashSet<>());
        existingBook.setBookTags(new HashSet<>());
        existingBook.setBookAuthors(new HashSet<>());

        // Publisher를 명시적으로 설정
        Publisher existingPublisher = new Publisher();
        existingPublisher.setPublisherId(2L);
        existingBook.setPublisher(existingPublisher);

        Publisher newPublisher = new Publisher();
        newPublisher.setPublisherId(1L);

        // 카테고리 설정
        Category category1 = new Category();
        category1.setCategoryId(1L);
        Category category2 = new Category();
        category2.setCategoryId(2L);

        // 태그 설정
        Tag tag1 = new Tag();
        tag1.setTagId(1L);
        Tag tag2 = new Tag();
        tag2.setTagId(2L);

        // 작가 설정
        Author author1 = new Author();
        author1.setAuthorId(1L);
        Author author2 = new Author();
        author2.setAuthorId(2L);

        // Mock 설정
        when(bookRepository.findBookByBookIsbn("1234567890123")).thenReturn(Optional.of(existingBook));
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(newPublisher));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category2));
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag1));
        when(tagRepository.findById(2L)).thenReturn(Optional.of(tag2));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author1)); // Mock 추가
        when(authorRepository.findById(2L)).thenReturn(Optional.of(author2)); // Mock 추가
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        // When: 메서드 실행
        BookUpdateResponse response = bookMgmtService.updateBook(bookUpdateRequest);

        // Then: 업데이트 검증
        assertEquals(1L, response.getBookId());
        assertEquals("Updated Book Name", response.getBookName());
        assertEquals("Updated Info", response.getBookInfo());
        assertEquals("판매 중", response.getBookStatus());
        assertEquals(100, response.getStock());
        assertEquals(18000, response.getSalePrice());

        // Mock 메서드 호출 검증
        verify(bookRepository).save(existingBook);
        verify(publisherRepository).findById(1L);
        verify(categoryRepository, times(2)).findById(anyLong());
        verify(tagRepository, times(2)).findById(anyLong());
        verify(authorRepository, times(2)).findById(anyLong()); // 호출 검증 추가
    }

    @Test
    void getBookById_shouldReturnBookInfoResponseWhenBookExists() {
        // Given: Mock 데이터 설정
        Long bookId = 1L;
        Book mockBook = new Book();
        mockBook.setBookId(bookId);
        mockBook.setBookName("Test Book");

        BookInfoResponse mockResponse = new BookInfoResponse();
        mockResponse.setBookId(bookId);
        mockResponse.setBookName("Test Book");

        // Mock 설정
        when(bookRepository.findByBookId(bookId)).thenReturn(Optional.of(mockBook));
        when(bookMapper.bookToBookInfoResponse(mockBook)).thenReturn(mockResponse);

        // When: 메서드 호출
        BookInfoResponse result = bookMgmtService.getBookById(bookId);

        // Then: 검증
        assertNotNull(result);
        assertEquals(bookId, result.getBookId());
        assertEquals("Test Book", result.getBookName());

        // Mock 호출 검증
        verify(bookRepository, times(1)).findByBookId(bookId);
        verify(bookMapper, times(1)).bookToBookInfoResponse(mockBook);
    }

    @Test
    void getBookById_shouldThrowBookNotFoundExceptionWhenBookDoesNotExist() {
        // Given: Mock 데이터 설정
        Long bookId = 1L;

        // Mock 설정
        when(bookRepository.findByBookId(bookId)).thenReturn(Optional.empty());

        // When & Then: 예외 검증
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookMgmtService.getBookById(bookId);
        });

        assertEquals(BOOK_ID_NOT_FOUND_MESSAGE, exception.getMessage());

        // Mock 호출 검증
        verify(bookRepository, times(1)).findByBookId(bookId);
        verifyNoInteractions(bookMapper);
    }


    @Test
    @Transactional // 테스트 메서드가 트랜잭션을 사용하도록 설정
    void deleteBook_success() {
        // given
        Book book = new Book();
        book.setBookStatus(BookStatus.ON_SALE);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        // when
        BookDeleteResponse response = bookMgmtService.deleteBook(1L);

        // then
        assertEquals(BookStatus.DELETE_BOOK, book.getBookStatus());
    }

    @Test
    void deleteBook_bookNotFound() {
        // given
        Long bookId = 2L;
        Mockito.when(bookRepository.findById(bookId))
            .thenReturn(Optional.empty());

        // when & then
        assertThrows(BookNotFoundException.class, () -> bookMgmtService.deleteBook(bookId));
        Mockito.verify(bookRepository, Mockito.never()).save(Mockito.any(Book.class));
    }
}