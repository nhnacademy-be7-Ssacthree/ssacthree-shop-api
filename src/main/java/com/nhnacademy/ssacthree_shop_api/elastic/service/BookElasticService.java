package com.nhnacademy.ssacthree_shop_api.elastic.service;

import com.nhnacademy.ssacthree_shop_api.elastic.client.ElasticFeignClient;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookElasticService {


  private final ElasticFeignClient elasticFeignClient;


  public void saveBookToElasticsearch(BookDocument bookDocument) {
    try {
      ResponseEntity<String> response = elasticFeignClient.saveBook(bookDocument);

      // sysout 모두 log, 전용 exception으로 변경해주기
      if (response.getStatusCode().is2xxSuccessful()) {
        System.out.println("Successfully saved to Elasticsearch: " + response.getBody());
      } else {
        System.err.println("Failed to save to Elasticsearch. Status code: " + response.getStatusCode());
      }
    } catch (Exception e) {
      System.err.println("Error occurred while saving to Elasticsearch: " + e.getMessage());
      e.printStackTrace();
    }
  }


  // 검색
  public String searchBooksInElasticsearch(String keyword, Pageable pageable, String sortField, String sortOrder) {
    String sortClause = "";
    if (sortField != null && sortOrder != null) {
      sortClause = "\n  \"sort\": [\n    { \"" + sortField + "\": \"" + sortOrder + "\" }\n  ],";
    }

    String query = "{\n" +
        " \"from\": " + pageable.getOffset() + ",\n" +
        " \"size\": " + pageable.getPageSize() + ",\n" +
        sortClause +
        "  \"query\": {\n" +
        "    \"multi_match\": {\n" +
        "      \"query\": \"" + keyword + "\",\n" +
        "      \"fields\": [\"bookName^100\", \"bookInfo^30\", \"bookIsbn^100\"]\n" +
        "    }\n" +
        "  }\n" +
        "}";

    try {
      ResponseEntity<String> response = elasticFeignClient.searchBooks(query);
      return response.getBody();
    } catch (Exception e) {
      e.printStackTrace();
      return "elastic error: Error occurred while searching";
    }
  }


  // 연결 확인용 ( 배포 시 삭제해도 됨)
  public String checkElasticsearchHealth() {
    try {
      ResponseEntity<String> response = elasticFeignClient.checkElasticHealth();
      if (response.getStatusCode().is2xxSuccessful()) {
        return "Elasticsearch is healthy: " + response.getBody();
      } else {
        return "Elasticsearch health check failed. Status code: " + response.getStatusCode();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "Error occurred while checking Elasticsearch health: " + e.getMessage();
    }
  }
}
