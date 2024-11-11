package com.nhnacademy.ssacthree_shop_api.elastic.client;

import com.nhnacademy.ssacthree_shop_api.config.FeignClientConfig;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// 서버 엘라스틱서치 주소
@FeignClient(name = "elasticsearch", url = "${elasticsearch.url}", configuration = FeignClientConfig.class)
public interface ElasticsearchFeignClient {

  // 데이터 검색
  @PostMapping("/ssacthree_books/_search")
  Map<String, Object> searchBooks(@RequestBody Map<String, Object> query);
  
//   데이터 저장
  @PostMapping("/ssacthree_books/_doc")
  void saveBook(BookDocument bookDocument);

  // ElasticSearch 인덱스 내의 자료를 삭제하는 매핑 (데이터 싱크를 위해 사용)
  // 현재 싱크를 맞추는 방식: 삭제 후 재생성
  @PostMapping("/ssacthree_books/_delete_by_query")
  void deleteAllDocuments(@RequestBody Map<String, Object> query);

  // bulk 구현 못함
//  @PostMapping("/_bulk")
//  void bulkSave(@RequestBody String bulkRequest);

}