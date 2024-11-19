package com.nhnacademy.ssacthree_shop_api.elastic.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ssacthree_shop_api.elastic.client.ElasticsearchFeignClient;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import com.nhnacademy.ssacthree_shop_api.elastic.dto.SearchRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.*;

@Slf4j // 로그를 기록하기 위한 Lombok 어노테이션
@Service // Spring의 서비스 계층 컴포넌트로 등록
@RequiredArgsConstructor // final로 선언된 필드에 대해 생성자 자동 생성
public class ElasticService {

  // Elasticsearch와의 통신을 담당하는 Feign Client
  private final ElasticsearchFeignClient elasticsearchFeignClient;

  // 상수 정의 (정렬, 필드 이름 등 재사용되는 값)
  private static final String QUERY = "query";
  private static final String ORDER = "order";
  private static final String DESC = "desc";
  private static final String ASC = "asc";
  private static final String PUBLICATION_DATE = "publicationDate";
  private static final String SALE_PRICE = "salePrice";
  private static final String BOOK_VIEW_COUNT = "bookViewCount";
  private static final String SCORE = "_score";

  /**
   * Elasticsearch 클러스터 상태 확인 메서드
   *
   * @return 클러스터 상태가 green 또는 yellow면 true, 그렇지 않으면 false
   */
  public boolean checkElasticsearchHealth() {
    try {
      Map<String, Object> healthResponse = elasticsearchFeignClient.getHealthStatus();
      String status = (String) healthResponse.get("status");
      // 클러스터 상태가 green 또는 yellow인 경우 정상으로 간주
      return "green".equalsIgnoreCase(status) || "yellow".equalsIgnoreCase(status);
    } catch (Exception e) {
      log.error("Elasticsearch health check failed", e);
      return false; // 오류 발생 시 false 반환
    }
  }

  /**
   * 책 검색 메서드
   * 사용자의 검색 요청을 Elasticsearch에 전달하고 결과를 처리
   *
   * @param searchRequest 검색 요청 정보 (키워드, 필터, 정렬 등 포함)
   * @return 검색된 책 리스트 (BookDocument 객체)
   */
  public List<BookDocument> searchBooks(SearchRequest searchRequest) {
    // 검색 요청 정보를 기반으로 Elasticsearch 쿼리 생성
    Map<String, Object> query = buildElasticsearchQuery(searchRequest);
    // 생성된 쿼리를 Elasticsearch에 전달하고 응답을 받음
    Map<String, Object> response = elasticsearchFeignClient.searchBooks(query);
    // 응답 데이터를 BookDocument 리스트로 변환
    return parseSearchResults(response);
  }

  /**
   * Elasticsearch 쿼리를 생성하는 메서드
   *
   * @param searchRequest 검색 요청 정보
   * @return Elasticsearch에서 사용할 쿼리 맵 객체
   */
  public Map<String, Object> buildElasticsearchQuery(SearchRequest searchRequest) {
    Map<String, Object> query = new HashMap<>();
    Map<String, Object> multiMatch = new HashMap<>();

    // Multi-match 쿼리 구성: 다양한 필드에서 키워드 검색
    multiMatch.put(QUERY, searchRequest.getKeyword());
    multiMatch.put("fields", List.of(
        "bookName^100", // bookName 필드에 높은 가중치 부여
        "bookInfo^30",  // bookInfo 필드에 중간 가중치 부여
        "tags^50",      // 태그 필드에 높은 가중치 부여
        "category^3",   // category 필드에 낮은 가중치 부여
        "bookName.jaso^80", // 초성 검색을 위한 jaso 필드
        "bookName.icu^50"   // 발음 유사 검색을 위한 icu 필드
    ));
    query.put(QUERY, Map.of("multi_match", multiMatch));

    // 페이지네이션 설정
//    int pageSize = searchRequest.getPageSize();
    int page = Math.max(0, searchRequest.getPage() - 1);
    int pageSize = Math.min(Math.max(1, searchRequest.getPageSize()), 100); // 최소 1, 최대 100 제한
    query.put("from", page * pageSize); // 시작점
    query.put("size", pageSize); // 결과 크기


    // 정렬 조건 추가
    if (searchRequest.getSort() != null) {
      query.put("sort", getSortCriteria(searchRequest.getSort())); // 정렬 기준 추가
    }

    // 필터 조건 추가 (카테고리, 태그 등)
    if (searchRequest.getFilters() != null && !searchRequest.getFilters().isEmpty()) {
      query.put("post_filter", getFilterConditions(searchRequest.getFilters()));
    }

    // Elasticsearch 쿼리를 로그로 출력
    logElasticsearchQuery(query);

    return query;
  }

  /**
   * 정렬 조건 구성 메서드
   *
   * @param sort 사용자가 요청한 정렬 방식
   * @return 정렬 조건 리스트
   */
  private List<Map<String, Object>> getSortCriteria(String sort) {
    return switch (sort) {
      case "newest" -> List.of(Map.of(PUBLICATION_DATE, Map.of(ORDER, DESC))); // 최신순
      case "priceLow" -> List.of(Map.of(SALE_PRICE, Map.of(ORDER, ASC))); // 낮은 가격순
      case "priceHigh" -> List.of(Map.of(SALE_PRICE, Map.of(ORDER, DESC))); // 높은 가격순
      case "popularity" -> List.of(Map.of(BOOK_VIEW_COUNT, Map.of(ORDER, DESC))); // 인기순
      default -> List.of(Map.of(SCORE, Map.of(ORDER, DESC))); // 기본: 정확도순
    };
  }

  /**
   * 필터 조건 구성 메서드
   *
   * @param filters 사용자가 요청한 필터 조건 (카테고리, 태그 등)
   * @return 필터 조건 맵 객체
   */
  private Map<String, Object> getFilterConditions(Map<String, String> filters) {
    List<Map<String, Object>> filterList = filters.entrySet().stream()
        .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty()) // 유효한 필터만 처리
        .map(entry -> {
          Map<String, Object> termMap = new HashMap<>();
          termMap.put("term", Map.of(entry.getKey(), entry.getValue())); // 필터 조건 추가
          return termMap;
        })
        .collect(Collectors.toList());

    // 필터 조건이 존재하면 bool 쿼리로 반환
    if (!filterList.isEmpty()) {
      return Map.of("bool", Map.of("must", filterList));
    }
    return new HashMap<>();
  }

  /**
   * Elasticsearch 응답 데이터를 BookDocument 리스트로 변환
   *
   * @param response Elasticsearch 검색 결과
   * @return 변환된 BookDocument 리스트
   */
  @SuppressWarnings("unchecked")
  private List<BookDocument> parseSearchResults(Map<String, Object> response) {
    List<BookDocument> books = new ArrayList<>(); // 결과 저장용 리스트

    // Elasticsearch 응답에서 "hits" 필드를 안전하게 추출
    Object hitsObject = response.get("hits");
    List<Map<String, Object>> hits = new ArrayList<>();
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

      // 각 필드의 데이터를 안전하게 추출
      book.setBookId(source.get("bookId") != null ? ((Number) source.get("bookId")).longValue() : null);
      book.setBookName((String) source.get("bookName"));
      book.setBookIndex((String) source.get("bookIndex"));
      book.setBookInfo((String) source.get("bookInfo"));
      book.setBookIsbn((String) source.get("bookIsbn"));
      convertToLocaldate(book, source.get("publicationDate") != null ? source.get("publicationDate").toString() : null); // 날짜형식 변환 후 저장.
      book.setRegularPrice(source.get("regularPrice") != null ? ((Number) source.get("regularPrice")).intValue() : null);
      book.setSalePrice(source.get("salePrice") != null ? ((Number) source.get("salePrice")).intValue() : null);
      book.setPacked((Boolean) source.getOrDefault("isPacked", source.get("packed")));
      book.setStock(source.get("stock") != null ? ((Number) source.get("stock")).intValue() : null);
      book.setBookThumbnailImageUrl((String) source.get("bookThumbnailImageUrl"));
      book.setBookViewCount(source.get("bookViewCount") != null ? ((Number) source.get("bookViewCount")).intValue() : null);
      book.setBookDiscount(source.get("bookDiscount") != null ? ((Number) source.get("bookDiscount")).intValue() : null);
      book.setPublisherNames((String) source.get("publisherNames"));
      book.setAuthorNames((String) source.get("authorNames"));




      // `tagNames`와 `category` 리스트 변환
      book.setTagNames(
          source.get("tagNames") != null
              ? ((List<?>) source.get("tagNames")).stream()
              .filter(String.class::isInstance)
              .map(String.class::cast)
              .collect(Collectors.toList())
              : Collections.emptyList()
      );

      book.setCategory(
          source.get("category") != null
              ? ((List<?>) source.get("category")).stream()
              .filter(String.class::isInstance)
              .map(String.class::cast)
              .collect(Collectors.toList())
              : Collections.emptyList()
      );

      books.add(book); // 리스트에 변환된 객체 추가
    }

    return books; // 변환된 BookDocument 리스트 반환
  }


  /**
   * 날짜 문자열을 "yyyy-MM-dd" 형식으로 변환한 후,
   * BookDocument 객체의 publicationDate 필드에 설정합니다.
   * @param bookDocument Elasticsearch 쿼리 맵 객체
   * @param publicationDateStr ISO 8601 형식의 날짜 문자열 (예: "2024-01-15T00:00:00.000Z").
   *                            null은 빈 값 그대로 사용
   */
  public void convertToLocaldate(BookDocument bookDocument, String publicationDateStr){
    if (publicationDateStr != null) {
      // ISO 8601 형식 문자열을 LocalDate로 변환 후 다시 문자열로 넣어줌 (시간 제외)
      String publicationDate = String.valueOf(LocalDate.parse(publicationDateStr.split("T")[0]));
      bookDocument.setPublicationDate(publicationDate);
    } else {
      bookDocument.setPublicationDate(null); // null인 경우 처리
    }
  }

  /**
   * Elasticsearch 쿼리를 JSON 형식으로 변환하여 로그로 출력
   *
   * @param query Elasticsearch 쿼리 맵 객체
   */
  private void logElasticsearchQuery(Map<String, Object> query) {
    try {
      // ObjectMapper를 사용하여 JSON 문자열로 변환
      ObjectMapper objectMapper = new ObjectMapper();
      String queryJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(query);
      log.info("Generated Elasticsearch Query: \n{}", queryJson);
    } catch (Exception e) {
      log.error("Failed to log Elasticsearch query", e);
    }
  }
}
