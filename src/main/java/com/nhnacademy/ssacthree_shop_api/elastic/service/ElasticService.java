package com.nhnacademy.ssacthree_shop_api.elastic.service;

import com.nhnacademy.ssacthree_shop_api.elastic.client.ElasticsearchFeignClient;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import java.util.HashMap;
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

  public List<BookDocument> searchBooks(String keyword, int page, String sort,
      Map<String, String> filters) {
    Map<String, Object> query = buildElasticsearchQuery(keyword, page, sort, filters);
    Map<String, Object> response = elasticsearchFeignClient.searchBooks(query);
    return parseSearchResults(response);
  }

  public BookDocument getBookById(Long id) {
    // Elasticsearch에서 ID로 도서 정보 가져오는 기능을 구현
    Map<String, Object> query = Map.of(
        "query", Map.of(
            "term", Map.of("bookId", id)
        )
    );
    Map<String, Object> response = elasticsearchFeignClient.searchBooks(query);

    List<BookDocument> books = parseSearchResults(response);
    return books.isEmpty() ? null : books.get(0);
  }

  // ElasticSerach에 요청할 검색쿼리를 생성합니다
  private Map<String, Object> buildElasticsearchQuery(String keyword, int page, String sort,
      Map<String, String> filters) {
    // Elasticsearch 쿼리 생성 로직 (기존과 동일)
    Map<String, Object> query = new HashMap<>();
    Map<String, Object> multiMatch = new HashMap<>();

    // Multi-match 쿼리 설정 (다양한 필드에 대해 검색)
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

    // 필터 적용
    if (filters != null && !filters.isEmpty()) {
      List<Map<String, Object>> filterList = new ArrayList<>();
      for (Map.Entry<String, String> filter : filters.entrySet()) {
        filterList.add(Map.of("term", Map.of(filter.getKey(), filter.getValue())));
      }
      query.put("post_filter", Map.of("bool", Map.of("must", filterList)));
    }

    return query;
  }


  // 검색 결과를 BookDocument 객체로 변환하는 메서드
  private List<BookDocument> parseSearchResults(Map<String, Object> response) {
    List<BookDocument> books = new ArrayList<>();

    // hits, 검색 대상들을 가져옴
    List<Map<String, Object>> hits = (List<Map<String, Object>>) ((Map<String, Object>) response.get(
        "hits")).get("hits");
    for (Map<String, Object> hit : hits) {
      Map<String, Object> source = (Map<String, Object>) hit.get("_source");  // _source 가져옴
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
      book.setTagNames((String) source.get("tags"));

      books.add(book); // 보여줄 결과 리스트에 하나씩 추가
    }

    return books;
  }
}
