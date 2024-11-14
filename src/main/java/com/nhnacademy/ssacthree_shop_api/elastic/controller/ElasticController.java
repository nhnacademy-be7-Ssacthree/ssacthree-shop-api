package com.nhnacademy.ssacthree_shop_api.elastic.controller;

import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import com.nhnacademy.ssacthree_shop_api.elastic.dto.SearchRequest;
import com.nhnacademy.ssacthree_shop_api.elastic.service.ElasticService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController  // 전체 Controller가 JSON 형식 응답을 반환하도록 설정
@RequestMapping("/api/shop/search")  // Search 경로로 접근
public class ElasticController {

  private final ElasticService elasticService;

  @GetMapping("/books")
  public ResponseEntity<List<BookDocument>> searchBooks(@RequestParam String keyword,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(required = false) String sort,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) String tag) {

    // `SearchRequest` 객체 생성
    Map<String, String> filters = new HashMap<>();
    if (category != null) {
      filters.put("category", category);
    }
    if (tag != null) {
      filters.put("tag", tag);
    }

    SearchRequest searchRequest = new SearchRequest(keyword, page, sort, filters);

    // ElasticService 호출
    List<BookDocument> books = elasticService.searchBooks(searchRequest);

    return ResponseEntity.ok(books);
  }
}

