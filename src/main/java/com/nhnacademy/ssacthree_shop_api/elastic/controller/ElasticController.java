package com.nhnacademy.ssacthree_shop_api.elastic.controller;

import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import com.nhnacademy.ssacthree_shop_api.elastic.service.BookSyncService;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.ssacthree_shop_api.elastic.service.BookElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Book Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/elasticsearch/books")
public class ElasticController {

  private final BookElasticService bookElasticService;
  private final BookSyncService bookSyncService;

////  JPA 저장 시 동시에 저장되게 하려면 없애기
//  @PostMapping("/save")
//  public String saveBookToElasticsearch(@RequestBody BookDocument bookDocument) {
//    bookElasticService.saveBookToElasticsearch(bookDocument);
//    return "Book saved to Elasticsearch.";
//  }

  @GetMapping("/search")
  public String searchBooks(
      @RequestParam String keyword,
      Pageable pageable,
      @RequestParam(required = false) String sortField,
      @RequestParam(required = false) String sortOrder) {
    return bookElasticService.searchBooksInElasticsearch(keyword, pageable, sortField, sortOrder);
  }


  // 연결 테스트용 //
  @GetMapping("/health")
  public String checkElasticsearchHealth() {
    return bookElasticService.checkElasticsearchHealth();
  }


  @GetMapping("/send-books-to-logstash")
  public String sendBooksToLogstash() {
    bookSyncService.sendBookDocumentsToLogstash();
    return "Book information sent to Logstash";
  }
}
