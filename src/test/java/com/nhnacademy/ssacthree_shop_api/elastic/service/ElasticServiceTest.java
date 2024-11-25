package com.nhnacademy.ssacthree_shop_api.elastic.service;

import com.nhnacademy.ssacthree_shop_api.elastic.client.ElasticsearchFeignClient;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import com.nhnacademy.ssacthree_shop_api.elastic.dto.SearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ElasticServiceTest {

  @Mock
  private ElasticsearchFeignClient elasticsearchFeignClient;

  @InjectMocks
  private ElasticService elasticService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void checkElasticsearchHealth_ShouldReturnTrueForGreenOrYellowStatus() {
    // Arrange
    when(elasticsearchFeignClient.getHealthStatus()).thenReturn(Map.of("status", "green"));

    // Act
    boolean isHealthy = elasticService.checkElasticsearchHealth();

    // Assert
    assertThat(isHealthy).isTrue();
    verify(elasticsearchFeignClient, times(1)).getHealthStatus();
  }

  @Test
  void checkElasticsearchHealth_ShouldReturnFalseForOtherStatus() {
    // Arrange
    when(elasticsearchFeignClient.getHealthStatus()).thenReturn(Map.of("status", "red"));

    // Act
    boolean isHealthy = elasticService.checkElasticsearchHealth();

    // Assert
    assertThat(isHealthy).isFalse();
    verify(elasticsearchFeignClient, times(1)).getHealthStatus();
  }

  @Test
  void searchBooksWithTotalCount_ShouldReturnSearchResults() {
    // Arrange
    SearchRequest searchRequest = new SearchRequest("keyword", 0, "relevance", 10, Map.of("category", "fiction"));
    Map<String, Object> mockResponse = Map.of(
        "hits", Map.of(
            "total", Map.of("value", 1),
            "hits", List.of(Map.of("_source", Map.of(
                "bookId", 1L,
                "bookName", "Test Book",
                "bookInfo", "This is a test book",
                "publicationDate", "2023-11-20",
                "salePrice", 1000,
                "tagNames", List.of("fiction"),
                "category", List.of("Literature")
            )))
        )
    );

    when(elasticsearchFeignClient.searchBooks(any())).thenReturn(mockResponse);

    // Act
    Map<String, Object> results = elasticService.searchBooksWithTotalCount(searchRequest);

    // Assert
    assertThat(results).isNotNull();
    assertThat(results.get("totalHits")).isEqualTo(1);
    List<BookDocument> books = (List<BookDocument>) results.get("books");
    assertThat(books).hasSize(1);
    assertThat(books.get(0).getBookName()).isEqualTo("Test Book");

    verify(elasticsearchFeignClient, times(1)).searchBooks(any());
  }

  @Test
  void buildElasticsearchQuery_ShouldBuildValidQuery() {
    // Arrange
    SearchRequest searchRequest = new SearchRequest("test", 0, "popularity", 10, Map.of("category", "fiction"));

    // Act
    Map<String, Object> query = elasticService.buildElasticsearchQuery(searchRequest);

    // Assert
    assertThat(query).isNotNull();
    assertThat(query.get("query")).isNotNull();
    assertThat(query.get("sort")).isNotNull();
  }

  @Test
  void parseSearchResults_ShouldReturnEmptyListForEmptyResponse() {
    // Arrange
    Map<String, Object> mockResponse = Map.of("hits", Map.of("hits", List.of()));

    // Act
    List<BookDocument> books = elasticService.parseSearchResults(mockResponse);

    // Assert
    assertThat(books).isEmpty();
  }

  @Test
  void parseSearchResults_ShouldHandleInvalidResponse() {
    // Arrange
    Map<String, Object> mockResponse = Map.of(); // Invalid response

    // Act
    List<BookDocument> books = elasticService.parseSearchResults(mockResponse);

    // Assert
    assertThat(books).isEmpty();
  }

  @Test
  void mapToBookDocument_ShouldHandleValidSource() {
    // Arrange
    Map<String, Object> source = Map.of(
        "bookId", 1L,
        "bookName", "Sample Book",
        "publicationDate", "2023-01-01T00:00:00Z",
        "salePrice", 200
    );

    // Act
    BookDocument book = elasticService.mapToBookDocument(source);

    // Assert
    assertThat(book.getBookName()).isEqualTo("Sample Book");
    assertThat(book.getSalePrice()).isEqualTo(200);
    assertThat(book.getPublicationDate()).isEqualTo("2023-01-01");
  }
}
