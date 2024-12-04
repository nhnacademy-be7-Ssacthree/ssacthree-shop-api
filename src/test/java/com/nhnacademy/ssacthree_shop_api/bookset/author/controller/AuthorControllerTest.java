package com.nhnacademy.ssacthree_shop_api.bookset.author.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.author.service.AuthorService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthorController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String AUTHOR_PATH = "/api/shop/admin/authors";
    private static final String AUTHOR_CREATE_SUCCESS_MESSAGE = "작가 정보 생성 성공";
    private static final String AUTHOR_UPDATE_SUCCESS_MESSAGE = "작가 정보 수정 성공";
    private static final String AUTHOR_DELETE_SUCCESS_MESSAGE = "작가 정보 삭제 성공";

    @Test
    void getAllAuthors_returnsPagedAuthors() throws Exception {
        // Given: Mock 데이터 설정
        List<AuthorGetResponse> mockAuthors = List.of(
            new AuthorGetResponse(1L, "Author 1", "Info 1"),
            new AuthorGetResponse(2L, "Author 2", "Info 2")
        );
        Page<AuthorGetResponse> mockPage = new PageImpl<>(mockAuthors);

        // Mocking authorService.getAllAuthors 호출 결과
        when(authorService.getAllAuthors(any(Pageable.class))).thenReturn(mockPage);

        // When: /authors GET 요청
        mockMvc.perform(get(AUTHOR_PATH)
                .param("page", "0")
                .param("size", "10")
                .param("sort", "authorId:asc")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // Then: HTTP 상태 200 확인
            .andExpect(jsonPath("$.content", hasSize(2))) // 반환된 페이지의 크기 확인
            .andExpect(jsonPath("$.content[0].authorId").value(1L)) // 첫 번째 작가 ID 확인
            .andExpect(jsonPath("$.content[0].authorName").value("Author 1")) // 첫 번째 작가 이름 확인
            .andExpect(jsonPath("$.content[1].authorId").value(2L)) // 두 번째 작가 ID 확인
            .andExpect(jsonPath("$.content[1].authorName").value("Author 2")); // 두 번째 작가 이름 확인

        // Service 호출 확인
        verify(authorService, times(1)).getAllAuthors(any(Pageable.class));
    }

    @Test
    void getAllAuthorList_returnsListOfAuthors() throws Exception {
        // Given: Mock 데이터 설정
        List<AuthorGetResponse> mockAuthors = List.of(
            new AuthorGetResponse(1L, "Author 1", "Info 1"),
            new AuthorGetResponse(2L, "Author 2", "Info 2")
        );

        // Mocking authorService.getAllAuthorList 호출 결과
        when(authorService.getAllAuthorList()).thenReturn(mockAuthors);

        // When: /lists GET 요청
        mockMvc.perform(get(AUTHOR_PATH + "/lists")
                .contentType(MediaType.APPLICATION_JSON))
            // Then: 상태 코드 확인 및 결과 검증
            .andExpect(status().isOk()) // HTTP 상태 200 확인
            .andExpect(jsonPath("$", hasSize(2))) // 반환된 리스트 크기 확인
            .andExpect(jsonPath("$[0].authorId").value(1L)) // 첫 번째 작가 ID 확인
            .andExpect(jsonPath("$[0].authorName").value("Author 1")) // 첫 번째 작가 이름 확인
            .andExpect(jsonPath("$[1].authorId").value(2L)) // 두 번째 작가 ID 확인
            .andExpect(jsonPath("$[1].authorName").value("Author 2")); // 두 번째 작가 이름 확인

        // Service 호출 확인
        verify(authorService, times(1)).getAllAuthorList();
    }

    @Test
    void getAuthor_returnsAuthorById() throws Exception {
        // Given: Mock 데이터 설정
        Long authorId = 1L;
        AuthorGetResponse mockAuthor = new AuthorGetResponse(authorId, "Author 1", "Info 1");

        // Mocking authorService.getAuthorById 호출 결과
        when(authorService.getAuthorById(authorId)).thenReturn(mockAuthor);

        // When: /{authorId} GET 요청
        mockMvc.perform(get(AUTHOR_PATH + "/1") // URL 경로에 authorId 포함
                .contentType(MediaType.APPLICATION_JSON))
            // Then: 상태 코드와 결과 검증
            .andExpect(status().isOk()) // HTTP 상태 200 확인
            .andExpect(jsonPath("$.authorId").value(authorId)) // 응답의 authorId 확인
            .andExpect(jsonPath("$.authorName").value("Author 1")) // 응답의 authorName 확인
            .andExpect(jsonPath("$.authorInfo").value("Info 1")); // 응답의 authorInfo 확인

        // Service 호출 확인
        verify(authorService, times(1)).getAuthorById(authorId);
    }

    @Test
    void createAuthor_returnsCreatedResponse() throws Exception {
        // Given: Mock 요청 데이터
        AuthorCreateRequest mockRequest = new AuthorCreateRequest("Author 1", "Info 1");
        Author mockAuthor = new Author("Author 1", "Info 1"); // Author 엔티티 반환값 생성

        // authorService.createAuthor 호출 시 반환값 설정
        when(authorService.createAuthor(any(AuthorCreateRequest.class))).thenReturn(mockAuthor);

        // When: / POST 요청
        mockMvc.perform(post(AUTHOR_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRequest))) // JSON으로 요청 본문 변환
            // Then: 상태 코드와 결과 검증
            .andExpect(status().isCreated()) // HTTP 상태 201 확인
            .andExpect(jsonPath("$.message").value(AUTHOR_CREATE_SUCCESS_MESSAGE)); // 응답 메시지 확인

        // Service 호출 확인
        verify(authorService, times(1)).createAuthor(any(AuthorCreateRequest.class));
    }

    @Test
    void updateAuthor_returnsUpdatedResponse() throws Exception {
        // Given: Mock 요청 데이터
        AuthorUpdateRequest mockRequest = new AuthorUpdateRequest(1L, "Updated Author",
            "Updated Info");
        Author mockAuthor = new Author(1L, "Updated Author", "Updated Info"); // 업데이트 후 예상 결과

        // authorService.updateAuthor 호출 시 반환값 설정
        when(authorService.updateAuthor(any(AuthorUpdateRequest.class))).thenReturn(mockAuthor);

        // When: / PUT 요청
        mockMvc.perform(put(AUTHOR_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRequest))) // JSON으로 요청 본문 변환
            // Then: 상태 코드와 결과 검증
            .andExpect(status().isOk()) // HTTP 상태 200 확인
            .andExpect(jsonPath("$.message").value(AUTHOR_UPDATE_SUCCESS_MESSAGE)); // 응답 메시지 확인

        // Service 호출 확인
        verify(authorService, times(1)).updateAuthor(any(AuthorUpdateRequest.class));
    }

    @Test
    void deleteAuthor_authorExists_returnsSuccessMessage() throws Exception {
        // Given: 존재하는 authorId 설정
        Long existingAuthorId = 1L;

        // 서비스 메서드가 정상적으로 호출된다고 가정
        doNothing().when(authorService).deleteAuthor(existingAuthorId);

        // When: DELETE 요청을 보내고
        mockMvc.perform(
                delete(AUTHOR_PATH + "/{authorId}", existingAuthorId)) // 엔드포인트 경로는 실제 매핑에 맞춰야 합니다.
            // Then: 성공 응답 메시지를 검증
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(AUTHOR_DELETE_SUCCESS_MESSAGE));

        // 서비스 메서드가 호출되었는지 확인
        verify(authorService, times(1)).deleteAuthor(existingAuthorId);
    }


}