package com.nhnacademy.ssacthree_shop_api.elastic.service;

import com.nhnacademy.ssacthree_shop_api.elastic.client.ElasticsearchFeignClient;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import com.nhnacademy.ssacthree_shop_api.elastic.dto.SearchRequest;
import java.util.HashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticService {

  private final ElasticsearchFeignClient elasticsearchFeignClient;

  // 상수 정의
  private static final String QUERY = "query";
  private static final String ORDER = "order";
  private static final String DESC = "desc";
  private static final String ASC = "asc";
  private static final String PUBLICATION_DATE = "publicationDate";
  private static final String SALE_PRICE = "salePrice";
  private static final String BOOK_VIEW_COUNT = "bookViewCount";
  private static final String SCORE = "_score";

  public boolean checkElasticsearchHealth() {
    try {
      Map<String, Object> healthResponse = elasticsearchFeignClient.getHealthStatus();
      String status = (String) healthResponse.get("status");
      return "green".equalsIgnoreCase(status) || "yellow".equalsIgnoreCase(status); // Green and yellow indicate a healthy state
    } catch (Exception e) {
      log.info("Elasticsearch health check failed", e);
      return false;
    }
  }


  /**
   * 책 검색을 수행하는 메서드
   *
   * @param searchRequest 검색 요청 정보
   * @return 검색된 BookDocument 리스트
   */
  public List<BookDocument> searchBooks(SearchRequest searchRequest) {
    // SearchRequest를 Elasticsearch 쿼리로 변환
    Map<String, Object> query = buildElasticsearchQuery(searchRequest);
    // Elasticsearch에서 검색 수행
    Map<String, Object> response = elasticsearchFeignClient.searchBooks(query);
    // 응답 결과를 BookDocument 리스트로 변환하여 반환
    return parseSearchResults(response);
  }

  /**
   * Elasticsearch 쿼리를 생성하는 메서드
   *
   * @param searchRequest 검색 요청 정보
   * @return Elasticsearch 쿼리 Map 객체
   */
  public Map<String, Object> buildElasticsearchQuery(SearchRequest searchRequest) {
    Map<String, Object> query = new HashMap<>();
    Map<String, Object> multiMatch = new HashMap<>();

    // Multi-match 쿼리 설정
    multiMatch.put(QUERY, searchRequest.getKeyword());
    multiMatch.put("fields", List.of("bookName^100", "bookInfo^30", "tags^50", "category^3"));
    query.put(QUERY, Map.of("multi_match", multiMatch));

    // 페이지네이션 설정
    int pageSize = searchRequest.getPageSize();
    query.put("from", (searchRequest.getPage() - 1) * pageSize);
    query.put("size", pageSize);

    // 정렬 기준 설정: 정렬 필드와 오름/내림차순 설정
    if (searchRequest.getSort() != null) {
      List<Map<String, Object>> sortCriteria = new ArrayList<>();
      switch (searchRequest.getSort()) {
        case "newest":
          sortCriteria.add(Map.of(PUBLICATION_DATE, Map.of(ORDER, DESC)));
          break;
        case "priceLow":
          sortCriteria.add(Map.of(SALE_PRICE, Map.of(ORDER, ASC)));
          break;
        case "priceHigh":
          sortCriteria.add(Map.of(SALE_PRICE, Map.of(ORDER, DESC)));
          break;
        case "popularity":
          sortCriteria.add(Map.of(BOOK_VIEW_COUNT, Map.of(ORDER, DESC)));
          break;
        default:
          sortCriteria.add(Map.of(SCORE, Map.of(ORDER, DESC)));
      }
      query.put("sort", sortCriteria);
    }

    // 필터 조건 설정
    if (searchRequest.getFilters() != null && !searchRequest.getFilters().isEmpty()) {
      List<Map<String, Object>> filterList = new ArrayList<>();
      for (Map.Entry<String, String> filter : searchRequest.getFilters().entrySet()) {
        if (filter.getValue() != null && !filter.getValue().isEmpty()) {
          filterList.add(Map.of("term", Map.of(filter.getKey(), filter.getValue())));
        }
      }
      if (!filterList.isEmpty()) {
        query.put("post_filter", Map.of("bool", Map.of("must", filterList)));
      }
    }

    return query;
  }

  /**
   * Elasticsearch 응답 결과를 BookDocument 리스트로 변환하는 메서드
   *
   * @param response Elasticsearch 검색 응답 결과
   * @return 변환된 BookDocument 리스트
   */
  @SuppressWarnings("unchecked")
  private List<BookDocument> parseSearchResults(Map<String, Object> response) {
    List<BookDocument> books = new ArrayList<>(); // 검색 결과를 저장할 리스트

    // hits 필드의 검색 결과 가져오기
    List<Map<String, Object>> hits = new ArrayList<>();
    Object hitsObject = response.get("hits");

    if (hitsObject instanceof Map) {
      Map<String, Object> hitsMap = (Map<String, Object>) hitsObject;
      Object innerHits = hitsMap.get("hits");

      if (innerHits instanceof List) {
        hits = (List<Map<String, Object>>) innerHits;
      }
    }

    // 각 히트 결과를 BookDocument 객체로 변환
    for (Map<String, Object> hit : hits) {
      Map<String, Object> source = (Map<String, Object>) hit.get("_source");
      BookDocument book = new BookDocument();

      // BookDocument 필드에 Elasticsearch 응답 데이터를 매핑 / 각 결과들을 캐스팅해서 저장해야 제대로 저장 됩니다.
      book.setBookId(source.get("bookId") != null ? ((Number) source.get("bookId")).longValue() : null);
      book.setBookName((String) source.get("bookName"));
      book.setBookIndex((String) source.get("bookIndex"));
      book.setBookInfo((String) source.get("bookInfo"));
      book.setBookIsbn((String) source.get("bookIsbn"));

      // 날짜 형식에 맞춰 필요 시 파싱
      if (source.get("publicationDate") != null) {
        book.setPublicationDate(source.get("publicationDate").toString());
      }

      book.setRegularPrice(source.get("regularPrice") != null ? ((Number) source.get("regularPrice")).intValue() : null);
      book.setSalePrice(source.get("salePrice") != null ? ((Number) source.get("salePrice")).intValue() : null);
      book.setPacked((Boolean) source.getOrDefault("isPacked", source.get("packed")));
      book.setStock(source.get("stock") != null ? ((Number) source.get("stock")).intValue() : null);
      book.setBookThumbnailImageUrl((String) source.get("bookThumbnailImageUrl"));
      book.setBookViewCount(source.get("bookViewCount") != null ? ((Number) source.get("bookViewCount")).intValue() : null);
      book.setBookDiscount(source.get("bookDiscount") != null ? ((Number) source.get("bookDiscount")).intValue() : null);
      book.setPublisherNames((String) source.get("publisherNames"));
      book.setAuthorNames((String) source.get("authorNames"));

      // tagNames와 category를 리스트로 변환
      book.setTagNames(((List<?>) source.get("tagNames")).stream()
          .filter(String.class::isInstance)
          .map(String.class::cast)
          .collect(Collectors.toList()));

      book.setCategory(((List<?>) source.get("category")).stream()
          .filter(String.class::isInstance)
          .map(String.class::cast)
          .collect(Collectors.toList()));

      books.add(book);
    }

    return books;
  }
}
