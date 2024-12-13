package com.nhnacademy.ssacthree_shop_api.bookset.publisher.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherGetResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.service.PublisherService;
import com.nhnacademy.ssacthree_shop_api.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublisherController.class)
@Import(SecurityConfig.class)
class PublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherService publisherService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllPublishers() throws Exception {
        Page<PublisherGetResponse> publishers = new PageImpl<>(
                List.of(new PublisherGetResponse(1L, "Publisher A", true),
                        new PublisherGetResponse(2L, "Publisher B", false)),
                PageRequest.of(0, 10),
                2);

        Mockito.when(publisherService.getAllPublishers(any())).thenReturn(publishers);

        mockMvc.perform(get("/api/shop/admin/publishers")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "publisherId:asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].publisherId").value(1L))
                .andExpect(jsonPath("$.content[0].publisherName").value("Publisher A"))
                .andExpect(jsonPath("$.content[0].publisherIsUsed").value(true));
    }

    @Test
    void getAllPublisherList() throws Exception {
        List<PublisherGetResponse> publisherList = List.of(
                new PublisherGetResponse(1L, "Publisher A", true),
                new PublisherGetResponse(2L, "Publisher B", false));

        Mockito.when(publisherService.getAllPublisherList()).thenReturn(publisherList);

        mockMvc.perform(get("/api/shop/admin/publishers/lists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].publisherId").value(1L))
                .andExpect(jsonPath("$[0].publisherName").value("Publisher A"))
                .andExpect(jsonPath("$[0].publisherIsUsed").value(true));
    }

    @Test
    void updatePublisher() throws Exception {
        // 요청 객체 생성
        PublisherUpdateRequest updateRequest = new PublisherUpdateRequest(1L);

        // Mock 반환 객체 생성
        Publisher mockPublisher = new Publisher(1L, "Updated Publisher", true);
        Mockito.when(publisherService.updatePublisher(any())).thenReturn(mockPublisher);

        // MockMvc 테스트 실행
        mockMvc.perform(put("/api/shop/admin/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("수정 성공"));
    }

    @Test
    void createPublisher() throws Exception {
        // 테스트 요청 생성
        PublisherCreateRequest createRequest = new PublisherCreateRequest();
        createRequest.setPublisherName("New Publisher");

        // 예상 응답값 설정
        Publisher mockPublisher = new Publisher(1L, "New Publisher", true);
        Mockito.when(publisherService.createPublisher(any())).thenReturn(mockPublisher);

        // MockMvc 테스트 실행
        mockMvc.perform(post("/api/shop/admin/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("생성 성공"));
    }
}
