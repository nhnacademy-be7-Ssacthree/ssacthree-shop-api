package com.nhnacademy.ssacthree_shop_api.elastic.service;

import com.nhnacademy.ssacthree_shop_api.elastic.client.ElasticsearchFeignClient;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import com.nhnacademy.ssacthree_shop_api.elastic.dto.SearchRequest;
import java.util.HashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ElasticService {

  private final ElasticsearchFeignClient elasticsearchFeignClient;

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
  private Map<String, Object> buildElasticsearchQuery(SearchRequest searchRequest) {
    Map<String, Object> query = new HashMap<>();
    Map<String, Object> multiMatch = new HashMap<>();

    // Multi-match 쿼리 설정
    multiMatch.put("query", searchRequest.getKeyword());
    multiMatch.put("fields", List.of("bookName^100", "bookInfo^10", "tags^50"));
    query.put("query", Map.of("multi_match", multiMatch));

    // 페이지네이션 설정
    query.put("from", (searchRequest.getPage() - 1) * 20);
    query.put("size", 20);

    // 정렬 기준 설정: 정렬 필드와 오름/내림차순 설정
    if (searchRequest.getSort() != null) {
      String sortField;
      String sortOrder;
      switch (searchRequest.getSort()) {
        case "newest":
          sortField = "publicationDate";
          sortOrder = "desc";  // 최신순은 내림차순
          break;
        case "priceLow":
          sortField = "salePrice";
          sortOrder = "asc";  // 낮은 가격순은 오름차순
          break;
        case "priceHigh":
          sortField = "salePrice";
          sortOrder = "desc";  // 높은 가격순은 내림차순
          break;
        default:
          sortField = "bookViewCount";  // 인기도 순서 (조회수 기준)
          sortOrder = "desc";
      }
      query.put("sort", List.of(Map.of(sortField, Map.of("order", sortOrder))));
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

    // 디버그용 쿼리 출력
    System.out.println("GeneratedElasticsearchQuery: " + query);
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

      // BookDocument 필드에 Elasticsearch 응답 데이터를 매핑
      book.setBookId((Long) source.get("bookId"));
      book.setBookName((String) source.get("bookName"));
      book.setBookIndex((String) source.get("bookIndex"));
      book.setBookInfo((String) source.get("bookInfo"));
      book.setBookIsbn((String) source.get("bookIsbn"));
      book.setPublicationDate((String) source.get("publicationDate")); // 날짜 형식에 맞춰 필요 시 파싱
      book.setRegularPrice((Integer) source.get("regularPrice"));
      book.setSalePrice((Integer) source.get("salePrice"));
      book.setPacked((Boolean) source.getOrDefault("isPacked", source.get("packed"))); // isPacked와 packed 둘 다 확인
      book.setStock((Integer) source.get("stock"));
      book.setBookThumbnailImageUrl((String) source.get("bookThumbnailImageUrl"));
      book.setBookViewCount((Integer) source.get("bookViewCount"));
      book.setBookDiscount((Integer) source.get("bookDiscount"));
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
