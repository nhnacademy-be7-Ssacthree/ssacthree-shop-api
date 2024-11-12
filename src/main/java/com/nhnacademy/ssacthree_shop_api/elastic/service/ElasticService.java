package com.nhnacademy.ssacthree_shop_api.elastic.service;

import com.nhnacademy.ssacthree_shop_api.elastic.client.ElasticsearchFeignClient;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ElasticService {

  private final ElasticsearchFeignClient elasticsearchFeignClient;

  @Autowired
  public ElasticService(ElasticsearchFeignClient elasticsearchFeignClient) {
    this.elasticsearchFeignClient = elasticsearchFeignClient;
  }

  /**
   * 책 검색을 수행하는 메서드
   *
   * @param keyword 검색할 키워드
   * @param page 페이지 번호
   * @param sort 정렬 기준
   * @param filters 추가 필터 조건
   * @return 검색된 BookDocument 리스트
   */
  public List<BookDocument> searchBooks(String keyword, int page, String sort,
      Map<String, String> filters) {
    // Elasticsearch 쿼리를 생성하고 검색 요청 전송
    Map<String, Object> query = buildElasticsearchQuery(keyword, page, sort, filters);
    Map<String, Object> response = elasticsearchFeignClient.searchBooks(query);
    return parseSearchResults(response);
  }

  /**
   * 책 ID로 Elasticsearch에서 책 정보를 가져오는 메서드
   *
   * @param id 검색할 책의 ID
   * @return 해당 ID의 BookDocument 객체 (존재하지 않으면 null 반환)
   */
  public BookDocument getBookById(Long id) {
    // 특정 책 ID에 해당하는 책 정보를 조회하는 쿼리
    Map<String, Object> query = Map.of(
        "query", Map.of(
            "term", Map.of("bookId", id)
        )
    );
    Map<String, Object> response = elasticsearchFeignClient.searchBooks(query);

    List<BookDocument> books = parseSearchResults(response);
    return books.isEmpty() ? null : books.get(0);
  }

  /**
   * Elasticsearch 검색 쿼리를 빌드하는 메서드
   *
   * @param keyword 검색 키워드
   * @param page 페이지 번호
   * @param sort 정렬 기준
   * @param filters 추가 필터 조건
   * @return 빌드된 Elasticsearch 쿼리 Map 객체
   */
  private Map<String, Object> buildElasticsearchQuery(String keyword, int page, String sort,
      Map<String, String> filters) {
    Map<String, Object> query = new HashMap<>();
    Map<String, Object> multiMatch = new HashMap<>();

    // Multi-match 쿼리 설정: 다양한 필드에서 키워드 검색
    multiMatch.put("query", keyword);
    multiMatch.put("fields", List.of("bookName^100", "bookInfo^10", "tags^50"));
    query.put("query", Map.of("multi_match", multiMatch));

    // 페이지네이션 설정
    query.put("from", (page - 1) * 20);
    query.put("size", 20);

    // 정렬 설정
    if (sort != null) {
      query.put("sort", List.of(Map.of(sort, Map.of("order", "asc"))));
    }

    // 필터 조건 추가
    if (filters != null && !filters.isEmpty()) {
      List<Map<String, Object>> filterList = new ArrayList<>();
      for (Map.Entry<String, String> filter : filters.entrySet()) {
        filterList.add(Map.of("term", Map.of(filter.getKey(), filter.getValue())));
      }
      query.put("post_filter", Map.of("bool", Map.of("must", filterList)));
    }

    return query;
  }

  /**
   * Elasticsearch 검색 결과를 BookDocument 객체 리스트로 변환하는 메서드
   *
   * @param response Elasticsearch 검색 응답 결과
   * @return BookDocument 객체 리스트
   */
  @SuppressWarnings("unchecked")
  private List<BookDocument> parseSearchResults(Map<String, Object> response) {
    List<BookDocument> books = new ArrayList<>();

    // hits 필드의 내용을 가져와 List<Map<String, Object>> 형식으로 변환
    List<Map<String, Object>> hits = new ArrayList<>(); // 기본값으로 빈 리스트 설정
    Object hitsObject = response.get("hits");

    // hits 필드가 Map 형태인지 확인
    if (hitsObject instanceof Map) {
      Map<String, Object> hitsMap = (Map<String, Object>) hitsObject; // 안전하게 Map<String, Object>로 캐스팅
      Object innerHits = hitsMap.get("hits");

      // innerHits 필드가 List 형태인지 확인 후 변환
      if (innerHits instanceof List) {
        hits = (List<Map<String, Object>>) innerHits; // List<Map<String, Object>>로 캐스팅
      }
    }

    // hits 리스트를 순회하면서 각 문서를 BookDocument로 변환
    for (Map<String, Object> hit : hits) {
      Map<String, Object> source = (Map<String, Object>) hit.get("_source");
      BookDocument book = new BookDocument();

      // 각 필드를 직접 long, int 등으로 캐스팅하여 변환합니다.
      book.setBookId((Long) source.get("bookId"));
      book.setBookName((String) source.get("bookName"));
      book.setBookIndex((String) source.get("bookIndex"));
      book.setBookInfo((String) source.get("bookInfo"));
      book.setBookIsbn((String) source.get("bookIsbn"));
      book.setPublicationDate((String) source.get("publicationDate"));
      book.setRegularPrice((Integer) source.get("regularPrice"));
      book.setSalePrice((Integer) source.get("salePrice"));
      book.setPacked((Boolean) source.get("packed"));
      book.setStock((Integer) source.get("stock"));
      book.setBookThumbnailImageUrl((String) source.get("bookThumbnailImageUrl"));
      book.setBookViewCount((Integer) source.get("bookViewCount"));
      book.setBookDiscount((Integer) source.get("bookDiscount"));
      book.setPublisherNames((String) source.get("publisherNames"));
      book.setAuthorNames((String) source.get("authorNames"));

      // tagNames와 category 필드를 List<String> 형식으로 변환
      book.setTagNames(((List<?>) source.get("tagNames")).stream()
          .filter(String.class::isInstance)   // String 타입만 필터링
          .map(String.class::cast)            // String 타입으로 캐스팅
          .collect(Collectors.toList()));     // List로 수집하여 설정

      book.setCategory(((List<?>) source.get("category")).stream()
          .filter(String.class::isInstance)   // String 타입만 필터링
          .map(String.class::cast)            // String 타입으로 캐스팅
          .collect(Collectors.toList()));     // List로 수집하여 설정

      books.add(book); // 결과 리스트에 BookDocument 추가
    }

    return books;
  }
}
