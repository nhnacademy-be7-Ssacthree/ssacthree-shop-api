package com.nhnacademy.ssacthree_shop_api.bookset.book.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookDeleteResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookSearchResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookUpdateResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookMgmtService;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookMgmtController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookMgmtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookMgmtService bookMgmtService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BOOK_CREATE_SUCCESS_MESSAGE = "도서 정보 생성 성공";
    private static final String BOOK_UPDATE_SUCCESS_MESSAGE = "도서 정보 수정 성공";
    private static final String BOOK_DELETE_SUCCESS_MESSAGE = "도서 정보 삭제 성공";
    private static final String ON_SALE = "판매 중";
    private static final String DELETED_BOOK = "삭제 도서:";
    private static final String BOOKS_PATH = "/api/shop/admin/books";


    @Test
    void getAllBooks_returnsPagedBooks() throws Exception {
        // Given: Mock 데이터 설정
        List<BookSearchResponse> mockBooks = List.of(
            new BookSearchResponse(1L, "Book 1", "Info 1", ON_SALE),
            new BookSearchResponse(2L, "Book 2", "Info 2", DELETED_BOOK)
        );
        Page<BookSearchResponse> mockPage = new PageImpl<>(mockBooks, PageRequest.of(0, 10), mockBooks.size());


        when(bookMgmtService.getAllBooks(any(Pageable.class))).thenReturn(mockPage);

        // When: /books GET 요청
        mockMvc.perform(get(BOOKS_PATH)
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "bookName")
                .contentType(MediaType.APPLICATION_JSON))
            // Then: 상태 코드와 결과 검증
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(2))) // 반환된 데이터 크기 검증
            .andExpect(jsonPath("$.content[0].bookId").value(1L)) // 첫 번째 데이터의 ID 검증
            .andExpect(jsonPath("$.content[0].bookName").value("Book 1")) // 첫 번째 데이터의 이름 검증
            .andExpect(jsonPath("$.content[0].bookInfo").value("Info 1")) // 첫 번째 데이터의 정보 검증
            .andExpect(jsonPath("$.content[0].bookStatus").value(ON_SALE)) // 첫 번째 데이터의 상태 검증
            .andExpect(jsonPath("$.content[1].bookId").value(2L)) // 두 번째 데이터의 ID 검증
            .andExpect(jsonPath("$.content[1].bookName").value("Book 2")) // 두 번째 데이터의 이름 검증
            .andExpect(jsonPath("$.content[1].bookInfo").value("Info 2")) // 두 번째 데이터의 정보 검증
            .andExpect(jsonPath("$.content[1].bookStatus").value(DELETED_BOOK)); // 두 번째 데이터의 상태 검증

        // 서비스 호출 검증
        verify(bookMgmtService, times(1)).getAllBooks(any(Pageable.class));
    }

    @Test
    void createBook_shouldReturnCreatedResponse() throws Exception {
        // Given: 요청 데이터와 기대 응답 설정
        BookSaveRequest request = new BookSaveRequest();
        request.setBookName("Effective Java");
        request.setBookIndex("Chapter 1, Chapter 2");
        request.setBookInfo("A guide to Java best practices.");
        request.setBookIsbn("123-4567890123");
        request.setPublicationDate(LocalDate.of(2024, 1, 1));
        request.setRegularPrice(30000);
        request.setSalePrice(25000);
        request.setPacked(true);
        request.setStock(100);
        request.setBookThumbnailImageUrl("https://example.com/image.jpg");
        request.setBookViewCount(500);
        request.setBookDiscount(20);
        request.setBookStatus("Available");
        request.setPublisherId(1L);
        request.setCategoryIdList(List.of(1L, 2L));
        request.setAuthorIdList(List.of(1L, 2L));
        request.setTagIdList(List.of(1L, 2L, 3L));

        // When: POST 요청 실행
        mockMvc.perform(post(BOOKS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))) // 요청 데이터를 JSON으로 변환
            // Then: 상태 코드와 응답 메시지 검증
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value(BOOK_CREATE_SUCCESS_MESSAGE));

        // 서비스 호출 검증
        verify(bookMgmtService, times(1)).saveBook(any(BookSaveRequest.class));
    }

    @Test
    void updateBook_shouldReturnOkOnValidRequest() throws Exception {
        // Given: 유효한 요청 데이터
        BookUpdateRequest mockRequest = new BookUpdateRequest();
        mockRequest.setBookId(2L);
        mockRequest.setBookName("Updated book");
        mockRequest.setBookIndex("Chapter 1, Chapter 2");
        mockRequest.setBookInfo("Updated book info");
        mockRequest.setBookIsbn("1234567890123");
        mockRequest.setPublicationDate(LocalDate.of(2024, 1, 1));
        mockRequest.setRegularPrice(30000);
        mockRequest.setSalePrice(27000);
        mockRequest.setPacked(true);
        mockRequest.setStock(100);
        mockRequest.setBookThumbnailImageUrl("https://example.com/image.jpg");
        mockRequest.setBookViewCount(500);
        mockRequest.setBookDiscount(10);
        mockRequest.setBookStatus(ON_SALE);
        mockRequest.setPublisherId(1L);
        mockRequest.setCategoryIdList(List.of(1L, 2L));
        mockRequest.setAuthorIdList(List.of(1L, 2L));
        mockRequest.setTagIdList(List.of(1L, 2L, 3L));

        BookUpdateResponse mockResponse = new BookUpdateResponse();
        mockResponse.setBookId(2L);
        mockResponse.setBookName("Updated book");
        mockResponse.setBookIndex("Chapter 1, Chapter 2");
        mockResponse.setBookInfo("Updated book info");
        mockResponse.setBookIsbn("1234567890123");
        mockResponse.setPublicationDate(LocalDate.of(2024, 1, 1).atStartOfDay());
        mockResponse.setRegularPrice(30000);
        mockResponse.setSalePrice(27000);
        mockResponse.setPacked(true);
        mockResponse.setStock(100);
        mockResponse.setBookThumbnailImageUrl("https://example.com/image.jpg");
        mockResponse.setBookViewCount(500);
        mockResponse.setBookDiscount(10);
        mockResponse.setBookStatus(ON_SALE);
        mockResponse.setPublisher(new PublisherNameResponse(1L, "Updated publisher"));
        mockResponse.setCategories(List.of(new CategoryNameResponse(1L, "Updated category")));
        mockResponse.setAuthors((List.of(new AuthorNameResponse(1L, "Updated author"))));
        mockResponse.setTags(List.of(new TagInfoResponse(1L, "Updated tag")));


        // Mock 서비스 동작 설정
        when(bookMgmtService.updateBook(any(BookUpdateRequest.class)))
            .thenReturn(mockResponse);

        mockMvc.perform(put(BOOKS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRequest))) // 요청 데이터를 JSON으로 변환

            // Then: 상태 코드와 응답 메시지 검증
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(BOOK_UPDATE_SUCCESS_MESSAGE));
        // 서비스 호출 검증
        verify(bookMgmtService, times(1)).updateBook(any(BookUpdateRequest.class));
    }

    @Test
    void deleteBook_shouldReturnOkOnValidRequest() throws Exception {
        // Given: 유효한 요청 데이터 및 Mock 응답
        Long bookId = 1L;

        BookDeleteResponse mockResponse = new BookDeleteResponse();
        mockResponse.setBookId(bookId);
        mockResponse.setBookName("Sample Book");
        mockResponse.setBookInfo("Sample Book Info");
        mockResponse.setBookStatus(BookStatus.DELETE_BOOK);
        mockResponse.setStock(0);
        mockResponse.setAuthors(List.of(new AuthorNameResponse("Author1"), new AuthorNameResponse("Author2")));

        // Mock 서비스 동작 설정
        when(bookMgmtService.deleteBook(bookId)).thenReturn(mockResponse);

        // When: DELETE 요청 실행
        mockMvc.perform(delete(BOOKS_PATH + "/" + bookId)
                .contentType(MediaType.APPLICATION_JSON))
            // Then: 상태 코드와 응답 데이터 검증
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(BOOK_DELETE_SUCCESS_MESSAGE));

        // 서비스 호출 검증
        verify(bookMgmtService, times(1)).deleteBook(bookId);
    }

    @Test
    void getBookByBookId_shouldReturnBookInfoResponse() throws Exception {
        // Given: 유효한 요청 데이터 및 Mock 응답
        Long bookId = 1L;

        BookInfoResponse mockResponse = new BookInfoResponse();
        mockResponse.setBookId(bookId);
        mockResponse.setBookName("Sample Book");
        mockResponse.setBookIndex("Chapter 1, Chapter 2");
        mockResponse.setBookInfo("Sample Book Info");
        mockResponse.setBookIsbn("1234567890123");
        mockResponse.setRegularPrice(30000);
        mockResponse.setSalePrice(27000);
        mockResponse.setStock(100);
        mockResponse.setBookThumbnailImageUrl("https://example.com/image.jpg");
        mockResponse.setBookViewCount(500);
        mockResponse.setBookDiscount(10);
        mockResponse.setBookStatus(ON_SALE);

        // Mock 서비스 동작 설정
        when(bookMgmtService.getBookById(bookId)).thenReturn(mockResponse);

        // When: GET 요청 실행
        mockMvc.perform(get(BOOKS_PATH + "/" + bookId)
                .contentType(MediaType.APPLICATION_JSON))
            // Then: 상태 코드와 응답 데이터 검증
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bookId").value(1L))
            .andExpect(jsonPath("$.bookName").value("Sample Book"))
            .andExpect(jsonPath("$.bookIndex").value("Chapter 1, Chapter 2"))
            .andExpect(jsonPath("$.bookInfo").value("Sample Book Info"))
            .andExpect(jsonPath("$.bookIsbn").value("1234567890123"))
            .andExpect(jsonPath("$.regularPrice").value(30000))
            .andExpect(jsonPath("$.salePrice").value(27000))
            .andExpect(jsonPath("$.stock").value(100))
            .andExpect(jsonPath("$.bookThumbnailImageUrl").value("https://example.com/image.jpg"))
            .andExpect(jsonPath("$.bookViewCount").value(500))
            .andExpect(jsonPath("$.bookDiscount").value(10))
            .andExpect(jsonPath("$.bookStatus").value(ON_SALE));

        // 서비스 호출 검증
        verify(bookMgmtService, times(1)).getBookById(bookId);
    }

}