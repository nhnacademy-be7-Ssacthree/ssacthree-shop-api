package com.nhnacademy.ssacthree_shop_api.bookset.book.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookCommonService;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.service.MemberService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class BookCommonControllerTest {

    @InjectMocks
    private BookCommonController bookCommonController;

    @Mock
    private BookCommonService bookCommonService;

    @Mock
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAvailableBooks() {

        BookListResponse mockBook = new BookListResponse();

        Page<BookListResponse> mockPage = new PageImpl<>(Collections.singletonList(mockBook));

        when(bookCommonService.getAllAvailableBooks(any())).thenReturn(mockPage);

        ResponseEntity<Page<BookListResponse>> response = bookCommonController.getAllAvailableBooks(
            0, 10, new String[]{"bookName:asc"});

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(bookCommonService, times(1)).getAllAvailableBooks(any());
    }

    @Test
    void getBookById() {

        BookInfoResponse mockBook = BookInfoResponse.builder()
            .bookId(1L)
            .bookName("Test Book")
            .build();

        when(bookCommonService.getBook(1L)).thenReturn(mockBook);

        ResponseEntity<BookInfoResponse> response = bookCommonController.getBookById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Book", response.getBody().getBookName());

        verify(bookCommonService, times(1)).getBook(1L);
    }

    @Test
    void getBooksByTitle() {

        BookListResponse mockBook = new BookListResponse();

        Page<BookListResponse> mockPage = new PageImpl<>(Collections.singletonList(mockBook));

        when(bookCommonService.getBooksByBookName(any(), eq("Test"))).thenReturn(mockPage);

        ResponseEntity<Page<BookListResponse>> response = bookCommonController.getBooksByTitle(0,
            10, new String[]{"bookName:asc"}, "Test");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(bookCommonService, times(1)).getBooksByBookName(any(), eq("Test"));
    }

    @Test
    void getBooksByCategoryId() {

        BookListResponse mockBook = new BookListResponse();

        Page<BookListResponse> mockPage = new PageImpl<>(Collections.singletonList(mockBook));

        when(bookCommonService.getBooksByCategoryId(any(), eq(1L))).thenReturn(mockPage);

        ResponseEntity<Page<BookListResponse>> response = bookCommonController.getBooksByCategoryId(
            0, 10, new String[]{"bookName:asc"}, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(bookCommonService, times(1)).getBooksByCategoryId(any(), eq(1L));
    }

    @Test
    void getBooksByAuthorId() {

        BookListResponse mockBook = new BookListResponse();

        Page<BookListResponse> mockPage = new PageImpl<>(Collections.singletonList(mockBook));

        when(bookCommonService.getBooksByAuthorId(any(), eq(1L))).thenReturn(mockPage);

        ResponseEntity<Page<BookListResponse>> response = bookCommonController.getBookByAuthorId(0,
            10, new String[]{"bookName:asc"}, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(bookCommonService, times(1)).getBooksByAuthorId(any(), eq(1L));
    }

    @Test
    void getBooksByTagId() {

        BookListResponse mockBook = new BookListResponse();

        Page<BookListResponse> mockPage = new PageImpl<>(Collections.singletonList(mockBook));

        when(bookCommonService.getBooksByTagId(any(), eq(2L))).thenReturn(mockPage);

        ResponseEntity<Page<BookListResponse>> response = bookCommonController.getBooksByTagId(0,
            10, new String[]{"bookName:asc"}, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(bookCommonService, times(1)).getBooksByTagId(any(), eq(2L));
    }

    @Test
    void getCategoriesByBookId() {

        CategoryNameResponse mockCategory = new CategoryNameResponse(1L, "Category 1");

        when(bookCommonService.getCategoriesByBookId(1L)).thenReturn(List.of(mockCategory));

        ResponseEntity<List<CategoryNameResponse>> response = bookCommonController.getCategoriesByBookId(
            1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Category 1", response.getBody().get(0).getCategoryName());

        verify(bookCommonService, times(1)).getCategoriesByBookId(1L);
    }
}
