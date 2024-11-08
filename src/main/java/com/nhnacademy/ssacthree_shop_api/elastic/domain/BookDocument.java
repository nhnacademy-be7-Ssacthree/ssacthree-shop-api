package com.nhnacademy.ssacthree_shop_api.elastic.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDocument {

  private long bookId;              // 도서 ID
  private String bookName;          // 도서 제목
  private String bookInfo;          // 도서 설명
  private String bookIsbn;          // ISBN
  private LocalDateTime publicationDate; // 출판일
  private int salePrice;            // 할인 가격
  private int bookDiscount;         // 할인율
  
  // 저장할 때 같이 넣어주는게 좋을 듯
//  private String publisherName;     // 출판사 이름 (검색에 필요한 경우만 포함)
//  private String authorName;        // 작가 이름 (검색에 필요한 경우만 포함)
}