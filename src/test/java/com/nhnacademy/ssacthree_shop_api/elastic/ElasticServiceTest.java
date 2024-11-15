package com.nhnacademy.ssacthree_shop_api.elastic;

import com.nhnacademy.ssacthree_shop_api.elastic.client.ElasticsearchFeignClient;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import com.nhnacademy.ssacthree_shop_api.elastic.dto.SearchRequest;
import com.nhnacademy.ssacthree_shop_api.elastic.service.ElasticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ElasticServiceTest {

  @InjectMocks
  private ElasticService elasticService;

  @Mock
  private ElasticsearchFeignClient elasticsearchFeignClient;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCheckElasticsearchHealth_GreenStatus() {
    // Mock the Elasticsearch health status response
    Map<String, Object> mockHealthResponse = Map.of("status", "green");
    when(elasticsearchFeignClient.getHealthStatus()).thenReturn(mockHealthResponse);

    // Call the service method
    boolean result = elasticService.checkElasticsearchHealth();

    // Verify the result and interactions
    assertTrue(result);
    verify(elasticsearchFeignClient, times(1)).getHealthStatus();
  }

  @Test
  void testCheckElasticsearchHealth_UnhealthyStatus() {
    // Mock the Elasticsearch health status response
    Map<String, Object> mockHealthResponse = Map.of("status", "red");
    when(elasticsearchFeignClient.getHealthStatus()).thenReturn(mockHealthResponse);

    // Call the service method
    boolean result = elasticService.checkElasticsearchHealth();

    // Verify the result and interactions
    assertFalse(result);
    verify(elasticsearchFeignClient, times(1)).getHealthStatus();
  }

  @Test
  void testSearchBooks_WithValidQuery() {
    // 검색 요청 설정
    SearchRequest searchRequest = new SearchRequest(
        "test",
        1,
        "popularity",
        20,
        Map.of("category", "fiction", "tag", "bestseller")
    );

    // Elasticsearch 응답 데이터를 구성
    Map<String, Object> sourceData = new HashMap<>();
    sourceData.put("bookId", 1L);
    sourceData.put("bookName", "Test Book");
    sourceData.put("bookInfo", "A book for testing.");
    sourceData.put("tagNames", Arrays.asList("fiction", "bestseller"));
    sourceData.put("category", Arrays.asList("Fiction"));
    sourceData.put("bookIsbn", "1234567890");
    sourceData.put("publicationDate", "2024-01-01");
    sourceData.put("regularPrice", 2000);
    sourceData.put("salePrice", 1500);
    sourceData.put("isPacked", true);
    sourceData.put("stock", 50);
    sourceData.put("bookThumbnailImageUrl", "http://example.com/image.jpg");
    sourceData.put("bookViewCount", 100);
    sourceData.put("bookDiscount", 10);
    sourceData.put("publisherNames", "Test Publisher");
    sourceData.put("authorNames", "Test Author");

    Map<String, Object> hitData = new HashMap<>();
    hitData.put("_source", sourceData);

    List<Map<String, Object>> hitsList = new ArrayList<>();
    hitsList.add(hitData);

    Map<String, Object> hitsMap = new HashMap<>();
    hitsMap.put("hits", hitsList);

    Map<String, Object> mockResponse = new HashMap<>();
    mockResponse.put("hits", hitsMap);

    // ElasticsearchFeignClient의 응답을 모의(mock)로 설정
    when(elasticsearchFeignClient.searchBooks(any())).thenReturn(mockResponse);

    // 서비스 메서드 호출
    List<BookDocument> books = elasticService.searchBooks(searchRequest);

    // 검증
    assertNotNull(books);
    assertEquals(1, books.size());
    BookDocument book = books.get(0);
    assertEquals(1L, book.getBookId());
    assertEquals("Test Book", book.getBookName());
    assertEquals("A book for testing.", book.getBookInfo());
    assertEquals(Arrays.asList("fiction", "bestseller"), book.getTagNames());
    assertEquals(Arrays.asList("Fiction"), book.getCategory());
    assertEquals(1500, book.getSalePrice());
    assertEquals("Test Publisher", book.getPublisherNames());
    assertEquals("Test Author", book.getAuthorNames());

    // 클라이언트와의 상호작용 확인
    verify(elasticsearchFeignClient, times(1)).searchBooks(any());
  }

  @Test
  void testSearchBooks_EmptyResults() {
    // Mock search request
    SearchRequest searchRequest = new SearchRequest(
        "nonexistent",
        1,
        null,
        20,
        Map.of()
    );

    // Mock Elasticsearch response with no hits
    Map<String, Object> mockResponse = Map.of(
        "hits", Map.of("hits", List.of())
    );
    when(elasticsearchFeignClient.searchBooks(any())).thenReturn(mockResponse);

    // Call the service method
    List<BookDocument> books = elasticService.searchBooks(searchRequest);

    // Assertions
    assertNotNull(books);
    assertTrue(books.isEmpty());

    // Verify interaction with the client
    verify(elasticsearchFeignClient, times(1)).searchBooks(any());
  }

  @Test
  void testBuildElasticsearchQuery() {
    // Mock search request
    SearchRequest searchRequest = new SearchRequest(
        "test",
        2,
        "newest",
        10,
        Map.of("category", "fiction", "tag", "bestseller")
    );

    // Call the private method via reflection (for testing query building logic)
    Map<String, Object> query = elasticService.buildElasticsearchQuery(searchRequest);

    // Assertions
    assertNotNull(query);
    assertEquals("test", ((Map<String, Object>) ((Map<String, Object>) query.get("query")).get("multi_match")).get("query"));
    assertEquals(10, query.get("size"));
    assertEquals(10, query.get("from")); // 2nd page (10 * (2 - 1))

    List<Map<String, Object>> sortCriteria = (List<Map<String, Object>>) query.get("sort");
    assertNotNull(sortCriteria);
    assertEquals("desc", ((Map<String, Object>) sortCriteria.get(0).get("publicationDate")).get("order"));

    Map<String, Object> postFilter = (Map<String, Object>) query.get("post_filter");
    List<Map<String, Object>> filters = (List<Map<String, Object>>) ((Map<String, Object>) postFilter.get("bool")).get("must");
    assertEquals(2, filters.size());
  }
}
