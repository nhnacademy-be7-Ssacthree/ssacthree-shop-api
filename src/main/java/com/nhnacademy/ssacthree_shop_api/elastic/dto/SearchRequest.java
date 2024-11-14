package com.nhnacademy.ssacthree_shop_api.elastic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * 프론트에서 받아오는 검색 요청 DTO 입니다.
 *
 */

@AllArgsConstructor
@Data
public class SearchRequest {
  private String keyword;    // 검색 키워드
  private int page;          // 페이지 번호
  private String sort;       // 정렬 기준
  private Map<String, String> filters; // 필터 (카테고리, 태그 등)
}
