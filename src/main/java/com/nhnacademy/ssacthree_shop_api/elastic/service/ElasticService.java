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

import java.util.*;

  /*
  * checkElasticsearchHealth: 연결 상태 확인
  * searchBooksWithTotalCount: 검색 메서드들을 수행
  * buildElasticsearchQuery: 엘라스틱서치에 요청할 쿼리문 작성
  * parseSearchResults: 받아온 검색 결과를 Response로 변환하는 작업
  * 
  */



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
   * Elasticsearch에서 검색 요청을 처리하고, 검색된 데이터와 전체 데이터 개수를 반환하는 메서드.
   *
   * @param searchRequest 검색 요청 정보 (키워드, 필터, 정렬 등 포함)
   * @return Map<String, Object>
   *         - "totalHits": 전체 데이터 개수 (long) -> 페이징 처리를 위한 데이터
   *         - "books": 현재 페이지의 검색 결과 리스트 (List<BookDocument>)
   */
  public Map<String, Object> searchBooksWithTotalCount(SearchRequest searchRequest) {
    // Elasticsearch 쿼리 생성
    Map<String, Object> query = buildElasticsearchQuery(searchRequest);

    // Elasticsearch로 요청 보냄
    Map<String, Object> response = elasticsearchFeignClient.searchBooks(query);

    // 전체 데이터 개수 추출
    Integer totalHits = ((Map<String, Object>) response.get("hits")).get("total") instanceof Map
        ? ((Number) ((Map<String, Object>) ((Map<String, Object>) response.get("hits")).get("total")).get("value")).intValue()
        : 0;

    // 검색된 데이터 추출
    List<BookDocument> books = parseSearchResults(response);

    // 결과 반환, (map 으로 전체 데이터 개수와 책 데이터를 구분해줌)
    Map<String, Object> result = new HashMap<>();
    result.put("totalHits", totalHits); // 전체 데이터 개수
    result.put("books", books);         // 검색된 책 데이터

    return result;
  }

  /**
   * Elasticsearch 쿼리를 생성하는 메서드
   *
   * @param searchRequest 검색 요청 정보
   * @return Elasticsearch에서 사용할 쿼리 맵 객체
   */
  public Map<String, Object> buildElasticsearchQuery(SearchRequest searchRequest) {
    Map<String, Object> boolQuery = new HashMap<>();

    // 1. 키워드 검색 조건 추가
    boolQuery.put("must", List.of(createKeywordSearchCondition(searchRequest.getKeyword())));

    // 2. 필터 조건 추가
    if (searchRequest.getFilters() != null && !searchRequest.getFilters().isEmpty()) {
      Map<String, Object> filterConditions = getFilterConditions(searchRequest.getFilters());
      if (!filterConditions.isEmpty()) {
        boolQuery.put("filter", filterConditions);
      }
    }

    // 3. 쿼리 구성
    Map<String, Object> query = new HashMap<>();
    query.put("query", Map.of("bool", boolQuery));
    query.put("track_total_hits", true); // 전체 데이터 개수 요청
    query.put("from", (searchRequest.getPage()) * searchRequest.getPageSize());
    query.put("size", searchRequest.getPageSize());


    // 4. 정렬 조건 추가
    if (searchRequest.getSort() != null) {
      query.put("sort", getSortCriteria(searchRequest.getSort()));
    }


    log.info("로그를 출력합니다.");
    // - Elasticsearch 쿼리를 로그로 출력
    logElasticsearchQuery(query);

    return query;
  }

  // multi_match 쿼리 작성
  public Map<String, Object> createKeywordSearchCondition(String keyword) {
    return Map.of(
        "multi_match", Map.of(
            "query", keyword,
            "fields", List.of(
                "bookName.nori^100",
                "bookName^15",
                "bookName.edge_ngram^30",
                "bookName.shingle^30",
                "bookName.ascii^10",
                "bookInfo^5",
                "bookInfo.nori^30",
                "authorNames^40",
                "authorNames.jaso^10",
                "publisherNames^10",
                "publisherNames.jaso^5",
                "category^5",
                "category.nori^15"
            )
        )
    );
  }


  /**
   * 정렬 조건 구성 메서드
   *
   * @param sort 사용자가 요청한 정렬 방식
   * @return 정렬 조건 리스트
   */
  public List<Map<String, Object>> getSortCriteria(String sort) {
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
  public Map<String, Object> getFilterConditions(Map<String, String> filters) {
    //controller filters에 에서 category, tag 로 넣어줌
    if (filters.containsKey("category") && filters.get("category") != null && !filters.get("category").isEmpty()) {
      // 카테고리가 입력된 경우, 카테고리 필터만 추가
      return Map.of("term", Map.of("category.keyword", filters.get("category"))); // keyword로 정확히 일치할 때만 검색이 됩니다.
    }

    if (filters.containsKey("tag") && filters.get("tag") != null && !filters.get("tag").isEmpty()) {
      // 태그가 입력된 경우, 태그 필터만 추가
      return Map.of("term", Map.of("tag.keyword", filters.get("tag")));
    }

    // 필터가 없는 경우 빈 Map 반환
    return new HashMap<>();
  }
  // 카테고리가 입력된 경우, 카테고리 필터만 추가

  // 태그가 입력된 경우, 태그 필터만 추가

  // 필터가 없는 경우 빈 Map 반환


  /**
   * Elasticsearch 응답 데이터를 BookDocument 리스트로 변환
   *
   * @param response Elasticsearch 검색 결과
   * @return 변환된 BookDocument 리스트
   */
  @SuppressWarnings("unchecked")
  public List<BookDocument> parseSearchResults(Map<String, Object> response) {
    List<BookDocument> books = new ArrayList<>(); // 결과 저장용 리스트

    // Elasticsearch 응답에서 "hits" 필드 추출
    Object hitsObject = response.get("hits");
    if (hitsObject instanceof Map) {
      Map<String, Object> hitsMap = (Map<String, Object>) hitsObject;
      Object innerHits = hitsMap.get("hits");

      // 검색된 문서(hits.hits)를 리스트로 변환
      if (innerHits instanceof List) {
        List<Map<String, Object>> hits = (List<Map<String, Object>>) innerHits;

        // 각 문서를 BookDocument로 변환
        for (Map<String, Object> hit : hits) {
          Map<String, Object> source = (Map<String, Object>) hit.get("_source");
          // -- 변환 수행 -- //
          books.add(mapToBookDocument(source)); // 변환 후 리스트에 추가
        }
      }
    }

    return books; // 변환된 BookDocument 리스트 반환
  }

  /**
   * Elasticsearch의 "_source" 데이터를 BookDocument 객체로 변환합니다.
   *
   * @param source Elasticsearch 문서의 "_source" 데이터
   * @return BookDocument 객체
   */
  public BookDocument mapToBookDocument(Map<String, Object> source) {
    BookDocument book = new BookDocument();

    // 개별 필드 매핑 (안전하게 추출)
    book.setBookId(source.get("bookId") != null ? ((Number) source.get("bookId")).longValue() : 0L);
    book.setBookName((String) source.get("bookName"));
    book.setBookIndex((String) source.get("bookIndex"));
    book.setBookInfo((String) source.get("bookInfo"));
    book.setBookIsbn((String) source.get("bookIsbn"));
    book.setPublicationDate(convertToLocaldate((String) source.get("publicationDate")));
    book.setRegularPrice(
        source.get("regularPrice") != null ? ((Number) source.get("regularPrice")).intValue() : null
    );
    book.setSalePrice(
        source.get("salePrice") != null ? ((Number) source.get("salePrice")).intValue() : null
    );
    book.setPacked((Boolean) source.getOrDefault("isPacked", false));
    book.setStock(source.get("stock") != null ? ((Number) source.get("stock")).intValue() : null);
    book.setBookThumbnailImageUrl((String) source.get("bookThumbnailImageUrl"));
    book.setBookViewCount(
        source.get("bookViewCount") != null ? ((Number) source.get("bookViewCount")).intValue() : null
    );
    book.setBookDiscount(
        source.get("bookDiscount") != null ? ((Number) source.get("bookDiscount")).intValue() : null
    );
    book.setPublisherNames((String) source.get("publisherNames"));
    book.setAuthorNames((String) source.get("authorNames"));

    // 태그와 카테고리 리스트 변환 (안전하게 추출)
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

    return book;
  }




  /**
   * Elasticsearch의 날짜 문자열을 "yyyy-MM-dd" 형식으로 변환하여 반환합니다.
   *
   * @param publicationDateStr ISO 8601 형식의 날짜 문자열 (예: "2024-01-15T00:00:00.000Z")
   * @return 변환된 날짜 문자열 ("yyyy-MM-dd"), null인 경우 null 반환
   */
  public String convertToLocaldate(String publicationDateStr) {
    if (publicationDateStr == null || publicationDateStr.isEmpty()) {
      return null; // null 또는 빈 문자열 처리
    }
    return LocalDate.parse(publicationDateStr.split("T")[0]).toString(); // 날짜만 추출하여 반환
  }

  /**
   * Elasticsearch 쿼리를 JSON 형식으로 변환하여 로그로 출력
   *
   * @param query Elasticsearch 쿼리 맵 객체
   */
  public void logElasticsearchQuery(Map<String, Object> query) {
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
