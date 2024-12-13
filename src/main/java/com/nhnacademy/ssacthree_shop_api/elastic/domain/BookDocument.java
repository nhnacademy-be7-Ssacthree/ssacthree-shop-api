package com.nhnacademy.ssacthree_shop_api.elastic.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.util.List;

/**
 * elasticsearch 검색 인덱스와 맞는 Document DTO 입니다.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDocument {

  @Id
  private long bookId;
  private String bookName;
  private String bookIndex;
  private String bookInfo;
  private String bookIsbn;
  private String publicationDate;
  private Integer regularPrice;
  private Integer salePrice;
  private boolean isPacked;
  private Integer stock;
  private String bookThumbnailImageUrl;
  private Integer bookViewCount;
  private Integer bookDiscount;
  private String publisherNames;
  private String authorNames;

  // 여러 태그를 저장할 수 있도록 List<String> 타입
  private List<String> tagNames;
  // 여러 카테고리를 저장할 수 있도록 List<String> 타입
  private List<String> category;
}
