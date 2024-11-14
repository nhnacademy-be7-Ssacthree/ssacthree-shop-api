package com.nhnacademy.ssacthree_shop_api.elastic.client;

import com.nhnacademy.ssacthree_shop_api.config.FeignClientConfig;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// 서버 엘라스틱서치 주소
@FeignClient(name = "elasticsearch", url = "${elasticsearch.url}", configuration = FeignClientConfig.class)
public interface ElasticsearchFeignClient {

  // 데이터 검색, Elasticsearch 쿼리 요청을 받는 POST 엔드포인트
  @GetMapping("/ssacthree_books/_search")
  Map<String, Object> searchBooks(@RequestBody Map<String, Object> query);
  
//   데이터 저장 (bulkAPI로 변경하며 주석처리함 241110_17시)
  @PostMapping("/ssacthree_books/_doc")
  void saveBook(BookDocument bookDocument);

}