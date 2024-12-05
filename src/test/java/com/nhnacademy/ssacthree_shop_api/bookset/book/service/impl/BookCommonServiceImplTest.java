package com.nhnacademy.ssacthree_shop_api.bookset.book.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.BookNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.domain.BookLike;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.request.BookLikeRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.response.BookLikeResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.repository.BookLikeRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.commons.exception.NotFoundException;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class BookCommonServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookLikeRepository bookLikeRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private BookCommonServiceImpl bookCommonService;

    public BookCommonServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBook_Success() {
        Long bookId = 1L;
        BookInfoResponse expectedResponse = new BookInfoResponse();

        when(bookRepository.findBookById(bookId)).thenReturn(expectedResponse);

        BookInfoResponse actualResponse = bookCommonService.getBook(bookId);

        assertEquals(expectedResponse, actualResponse);
        verify(bookRepository, times(1)).findBookById(bookId);
    }

    @Test
    void testGetBook_NotFound() {
        Long bookId = 1L;

        when(bookRepository.findBookById(bookId)).thenThrow(
            new BookNotFoundException("Book not found"));

        assertThrows(BookNotFoundException.class, () -> bookCommonService.getBook(bookId));
        verify(bookRepository, times(1)).findBookById(bookId);
    }

    @Test
    void testSaveBookLike_Success() {
        Long bookId = 1L;
        Long customerId = 100L;
        BookLikeRequest bookLikeRequest = new BookLikeRequest();
        Book book = new Book();
        book.setBookId(bookId);
        Customer customer = new Customer();
        Member member = new Member();
        member.setCustomer(customer);

        when(bookRepository.findById(bookLikeRequest.getBookId())).thenReturn(Optional.of(book));
        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(bookLikeRepository.save(any(BookLike.class))).thenAnswer(
            invocation -> invocation.getArgument(0));

        BookLikeResponse response = bookCommonService.saveBookLike(bookLikeRequest, customerId);

        assertNotNull(response);
        verify(bookLikeRepository, times(1)).save(any(BookLike.class));
    }

    @Test
    void testSaveBookLike_BookNotFound() {
        Long customerId = 100L;
        BookLikeRequest bookLikeRequest = new BookLikeRequest();

        when(bookRepository.findById(bookLikeRequest.getBookId())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
            () -> bookCommonService.saveBookLike(bookLikeRequest, customerId));

        verify(bookRepository, times(1)).findById(bookLikeRequest.getBookId());
    }

    @Test
    void testDeleteBookLike_Success() {
        Long bookId = 1L;
        Long customerId = 100L;

        when(bookLikeRepository.existsById(any())).thenReturn(true);

        Boolean result = bookCommonService.deleteBookLike(bookId, customerId);

        assertTrue(result);
        verify(bookLikeRepository, times(1)).existsById(any());
        verify(bookLikeRepository, times(1)).deleteById(any());
    }

    @Test
    void testDeleteBookLike_NotFound() {
        Long bookId = 1L;
        Long customerId = 100L;

        when(bookLikeRepository.existsById(any())).thenReturn(false);

        assertThrows(NotFoundException.class,
            () -> bookCommonService.deleteBookLike(bookId, customerId));
        verify(bookLikeRepository, times(1)).existsById(any());
        verify(bookLikeRepository, never()).deleteById(any());
    }


    @Test
    void testGetCategoriesByBookId_Success() {
        Long bookId = 1L;
        List<CategoryNameResponse> expectedCategories = List.of(
            new CategoryNameResponse("Fiction"));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));
        when(bookRepository.findCategoriesByBookId(bookId)).thenReturn(expectedCategories);

        List<CategoryNameResponse> actualCategories = bookCommonService.getCategoriesByBookId(
            bookId);

        assertEquals(expectedCategories, actualCategories);
        verify(bookRepository, times(1)).findCategoriesByBookId(bookId);
    }

    @Test
    void testGetCategoriesByBookId_NotFound() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
            () -> bookCommonService.getCategoriesByBookId(bookId));
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testGetLikedBooksIdForCurrentUser_Success() {
        Long customerId = 100L;
        List<Long> expectedLikedBooks = List.of(1L, 2L, 3L);

        when(bookRepository.findLikedBookIdByCustomerId(customerId)).thenReturn(expectedLikedBooks);

        List<Long> actualLikedBooks = bookCommonService.getLikedBooksIdForCurrentUser(customerId);

        assertEquals(expectedLikedBooks, actualLikedBooks);
        verify(bookRepository, times(1)).findLikedBookIdByCustomerId(customerId);
    }

    @Test
    void testGetBooksByMemberId_Success() {
        Long customerId = 100L;
        Pageable pageable = Pageable.ofSize(10);
        Page<BookListResponse> expectedPage = new PageImpl<>(Collections.emptyList());

        when(bookRepository.findBookLikesByCustomerId(customerId, pageable)).thenReturn(
            expectedPage);

        Page<BookListResponse> actualPage = bookCommonService.getBooksByMemberId(pageable,
            customerId);

        assertEquals(expectedPage, actualPage);
        verify(bookRepository, times(1)).findBookLikesByCustomerId(customerId, pageable);
    }

    @Test
    void testGetBooksByTagId_Success() {
        Long tagId = 10L;
        Pageable pageable = Pageable.ofSize(10);
        Page<BookListResponse> expectedPage = new PageImpl<>(Collections.emptyList());

        when(bookRepository.findBooksByTagId(tagId, pageable)).thenReturn(expectedPage);

        Page<BookListResponse> actualPage = bookCommonService.getBooksByTagId(pageable, tagId);

        assertEquals(expectedPage, actualPage);
        verify(bookRepository, times(1)).findBooksByTagId(tagId, pageable);
    }

    @Test
    void testGetBooksByCategoryId_Success() {
        Long categoryId = 5L;
        Pageable pageable = Pageable.ofSize(10);
        Page<BookListResponse> expectedPage = new PageImpl<>(Collections.emptyList());

        when(bookRepository.findBooksByCategoryId(categoryId, pageable)).thenReturn(expectedPage);

        Page<BookListResponse> actualPage = bookCommonService.getBooksByCategoryId(pageable,
            categoryId);

        assertEquals(expectedPage, actualPage);
        verify(bookRepository, times(1)).findBooksByCategoryId(categoryId, pageable);
    }

    @Test
    void testGetBooksByAuthorId_Success() {
        Long authorId = 3L;
        Pageable pageable = Pageable.ofSize(10);
        Page<BookListResponse> expectedPage = new PageImpl<>(Collections.emptyList());

        when(bookRepository.findBooksByAuthorId(authorId, pageable)).thenReturn(expectedPage);

        Page<BookListResponse> actualPage = bookCommonService.getBooksByAuthorId(pageable,
            authorId);

        assertEquals(expectedPage, actualPage);
        verify(bookRepository, times(1)).findBooksByAuthorId(authorId, pageable);
    }

    @Test
    void testGetBooksByBookIsbn_Success() {
        String isbn = "123-456-789";
        BookInfoResponse expectedBookInfo = new BookInfoResponse();

        when(bookRepository.findByBookIsbn(isbn)).thenReturn(expectedBookInfo);

        BookInfoResponse actualBookInfo = bookCommonService.getBooksByBookIsbn(isbn);

        assertEquals(expectedBookInfo, actualBookInfo);
        verify(bookRepository, times(1)).findByBookIsbn(isbn);
    }

    @Test
    void testGetBooksByBookName_Success() {
        String bookName = "Test Book";
        Pageable pageable = Pageable.ofSize(10);
        Page<BookListResponse> expectedPage = new PageImpl<>(Collections.emptyList());

        when(bookRepository.findBooksByBookName(pageable, bookName)).thenReturn(expectedPage);

        Page<BookListResponse> actualPage = bookCommonService.getBooksByBookName(pageable,
            bookName);

        assertEquals(expectedPage, actualPage);
        verify(bookRepository, times(1)).findBooksByBookName(pageable, bookName);
    }

    @Test
    void testGetAllAvailableBooks_Success() {
        Pageable pageable = Pageable.ofSize(10);
        Page<BookListResponse> expectedPage = new PageImpl<>(Collections.emptyList());

        when(bookRepository.findAllAvailableBooks(pageable)).thenReturn(expectedPage);

        Page<BookListResponse> actualPage = bookCommonService.getAllAvailableBooks(pageable);

        assertEquals(expectedPage, actualPage);
        verify(bookRepository, times(1)).findAllAvailableBooks(pageable);
    }

}
