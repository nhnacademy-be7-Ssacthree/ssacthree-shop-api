package com.nhnacademy.ssacthree_shop_api.bookset.tag.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.request.TagUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.service.TagService;
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

@WebMvcTest(TagController.class)
@AutoConfigureMockMvc(addFilters = false)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TAG_PATH = "/api/shop/admin/tags";
    private static final String TAG_UPDATE_SUCCESS_MESSAGE = "태그 정보 수정 성공";
    private static final String TAG_DELETE_SUCCESS_MESSAGE = "태그 정보 삭제 성공";

    @Test
    void getAllTags_returnsPagedTags() throws Exception {
        // Given: Mock 데이터 설정
        List<TagInfoResponse> mockTags = List.of(
            new TagInfoResponse(1L, "Tag 1"),
            new TagInfoResponse(2L, "Tag 2")
        );
        Page<TagInfoResponse> mockPage = new PageImpl<>(mockTags);

        when(tagService.getAllTags(any(Pageable.class))).thenReturn(mockPage);

        // When: /tags GET 요청
        mockMvc.perform(get(TAG_PATH)
                .param("page", "0")
                .param("size", "10")
                .param("sort", "tagId:asc")
                .contentType(MediaType.APPLICATION_JSON))
            // Then: 상태 코드와 결과 검증
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(2)))
            .andExpect(jsonPath("$.content[0].tagId").value(1L))
            .andExpect(jsonPath("$.content[0].tagName").value("Tag 1"))
            .andExpect(jsonPath("$.content[1].tagId").value(2L))
            .andExpect(jsonPath("$.content[1].tagName").value("Tag 2"));

        verify(tagService, times(1)).getAllTags(any(Pageable.class));
    }

    @Test
    void getAllTagList_returnsListOfTags() throws Exception {
        // Given: Mock 데이터 설정
        List<TagInfoResponse> mockTags = List.of(
            new TagInfoResponse(1L, "Tag 1"),
            new TagInfoResponse(2L, "Tag 2")
        );

        when(tagService.getAllTagList()).thenReturn(mockTags);

        // When: /lists GET 요청
        mockMvc.perform(get(TAG_PATH + "/lists")
                .contentType(MediaType.APPLICATION_JSON))
            // Then: 상태 코드와 결과 검증
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].tagId").value(1L))
            .andExpect(jsonPath("$[0].tagName").value("Tag 1"))
            .andExpect(jsonPath("$[1].tagId").value(2L))
            .andExpect(jsonPath("$[1].tagName").value("Tag 2"));

        verify(tagService, times(1)).getAllTagList();
    }


    @Test
    void updateTag_returnsUpdatedResponse() throws Exception {
        // Given: Mock 요청 데이터
        TagUpdateRequest mockRequest = new TagUpdateRequest(1L, "Updated Tag");
        TagInfoResponse mockResponse = new TagInfoResponse(1L, "Updated Tag");

        // tagService.updateTag 호출 시 반환값 설정
        when(tagService.updateTag(any(TagUpdateRequest.class))).thenReturn(mockResponse);

        // When: / PUT 요청
        mockMvc.perform(put(TAG_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRequest))) // 요청 본문 JSON 변환
            // Then: 상태 코드와 응답 메시지 검증
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(TAG_UPDATE_SUCCESS_MESSAGE)); // 성공 메시지 확인

        // Service 호출 검증
        verify(tagService, times(1)).updateTag(any(TagUpdateRequest.class));
    }

    @Test
    void deleteTag_returnsSuccessMessage() throws Exception {
        // Given: Mock 태그 ID 설정
        Long tagId = 1L;

        doNothing().when(tagService).deleteTag(tagId);

        // When: DELETE 요청
        mockMvc.perform(delete(TAG_PATH + "/{tag-id}", tagId)
                .contentType(MediaType.APPLICATION_JSON))
            // Then: 상태 코드와 결과 검증
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(TAG_DELETE_SUCCESS_MESSAGE));

        verify(tagService, times(1)).deleteTag(tagId);
    }
}
