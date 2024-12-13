package com.nhnacademy.ssacthree_shop_api.elastic.controller;

import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import com.nhnacademy.ssacthree_shop_api.elastic.dto.SearchRequest;
import com.nhnacademy.ssacthree_shop_api.elastic.dto.SearchResponse;
import com.nhnacademy.ssacthree_shop_api.elastic.service.ElasticService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController  // 전체 Controller가 JSON 형식 응답을 반환하도록 설정
@RequestMapping("/api/shop/search")  // Search 경로로 접근
public class ElasticController {

  private final ElasticService elasticService;

  @GetMapping("/health")
  public ResponseEntity<String> checkElasticsearchHealth() {
    boolean isHealthy = elasticService.checkElasticsearchHealth();
    if (isHealthy) {
      return ResponseEntity.ok("Elasticsearch is healthy");
    } else {
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Elasticsearch is unavailable");
    }
  }


  @GetMapping("/books")
  public ResponseEntity<SearchResponse> searchBooks(@RequestParam String keyword,
                                                        @RequestParam int page,
                                                        @RequestParam(required = false) String sort,
                                                        @RequestParam(required = false, defaultValue = "20") int pageSize,
                                                        @RequestParam(required = false) String category,
                                                        @RequestParam(required = false) String tag) {
    log.info("검색 요청: keyword={}, page={}, sort={}, pageSize={}, category={}, tag={}",
        keyword, page, sort, pageSize, category, tag);    // `SearchRequest` 객체 생성
    
    // 필터 조건 생성 / 둘 중 하나만 (현재는 카테고리만 구현되어있음)  -> 프론트, 백엔드 중복이라 없애도 될듯 ( 후순위 나중에 수정 )
    Map<String, String> filters = new HashMap<>();
    if (category != null && !category.isEmpty()) filters.put("category", category);
    if (tag != null && !tag.isEmpty()) filters.put("tag", tag);


    // 이걸 이렇게 dto로 만들고 보내줘야할까?
    SearchRequest searchRequest = new SearchRequest(keyword, page, sort, pageSize, filters);

    // ElasticService 호출
    Map<String, Object> searchResults = elasticService.searchBooksWithTotalCount(searchRequest);

    // DTO로 응답 데이터 구성
    SearchResponse response = new SearchResponse(
        (Integer) searchResults.get("totalHits"),               // 전체 데이터 개수
        (List<BookDocument>) searchResults.get("books")      // 검색된 데이터 리스트
    );


    return ResponseEntity.ok(response);
  }
}

