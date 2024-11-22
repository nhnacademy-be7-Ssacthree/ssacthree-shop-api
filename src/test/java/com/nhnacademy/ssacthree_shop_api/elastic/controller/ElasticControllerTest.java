package com.nhnacademy.ssacthree_shop_api.elastic.controller;


import com.nhnacademy.ssacthree_shop_api.config.SecurityConfig;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import com.nhnacademy.ssacthree_shop_api.elastic.dto.SearchResponse;
import com.nhnacademy.ssacthree_shop_api.elastic.service.ElasticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(ElasticController.class)
@Import(SecurityConfig.class) // Security 설정 로드
class ElasticControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ElasticService elasticService;

  @Test
  void testCheckElasticsearchHealth_Healthy() throws Exception {
    // Arrange
    when(elasticService.checkElasticsearchHealth()).thenReturn(true);

    // Act & Assert
    mockMvc.perform(get("/api/shop/search/health"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value("Elasticsearch is healthy"));
  }

  @Test
  void testCheckElasticsearchHealth_Unhealthy() throws Exception {
    // Arrange
    when(elasticService.checkElasticsearchHealth()).thenReturn(false);

    // Act & Assert
    mockMvc.perform(get("/api/shop/search/health"))
        .andExpect(status().isServiceUnavailable())
        .andExpect(jsonPath("$").value("Elasticsearch is unavailable"));
  }

  @Test
  void testSearchBooks() throws Exception {
    // Arrange
    String keyword = "test";
    int page = 1;
    int pageSize = 10;

    BookDocument bookDocument = new BookDocument();
    bookDocument.setBookId(1L);
    bookDocument.setBookName("Sample Book");
    bookDocument.setBookInfo("A test book description.");

    when(elasticService.searchBooksWithTotalCount(any())).thenReturn(Map.of(
        "totalHits", 1,
        "books", List.of(bookDocument)
    ));

    // Act & Assert
    mockMvc.perform(get("/api/shop/search/books")
            .param("keyword", keyword)
            .param("page", String.valueOf(page))
            .param("pageSize", String.valueOf(pageSize))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalHits").value(1))
        .andExpect(jsonPath("$.books[0].bookName").value("Sample Book"));
  }
}
