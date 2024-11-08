package com.nhnacademy.ssacthree_shop_api.elastic.client;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

// 서버 엘라스틱서치 주소 넣어주기 (properties로 변경해줘야하나?)
@FeignClient(name = "elasticsearch", url = "http://localhost:9200")
public interface ElasticsearchFeignClient {
  @PostMapping("/books/_doc")
  void saveBook(BookDocument bookDocument);
}