package com.nhnacademy.ssacthree_shop_api.elastic.dto;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SearchRequestTest {

  @Test
  void testNoArgsConstructor() {
    // Test for @NoArgsConstructor
    SearchRequest request = new SearchRequest(null, 0, null, 0, null);
    assertNotNull(request);
    assertThat(request.getKeyword()).isNull();
    assertThat(request.getPage()).isZero();
    assertThat(request.getSort()).isNull();
    assertThat(request.getPageSize()).isZero();
    assertThat(request.getFilters()).isNull();
  }

  @Test
  void testAllArgsConstructor() {
    // Test for @AllArgsConstructor
    Map<String, String> filters = new HashMap<>();
    filters.put("category", "Fiction");
    filters.put("tag", "Bestseller");

    SearchRequest request = new SearchRequest("Java", 1, "asc", 10, filters);

    assertNotNull(request);
    assertEquals("Java", request.getKeyword());
    assertEquals(1, request.getPage());
    assertEquals("asc", request.getSort());
    assertEquals(10, request.getPageSize());
    assertEquals(filters, request.getFilters());
  }

  @Test
  void testGetterAndSetter() {
    // Test for @Data (Getter and Setter)
    SearchRequest request = new SearchRequest(null, 0, null, 0, null);
    Map<String, String> filters = new HashMap<>();
    filters.put("category", "Fiction");

    request.setKeyword("Java");
    request.setPage(2);
    request.setSort("desc");
    request.setPageSize(20);
    request.setFilters(filters);

    assertEquals("Java", request.getKeyword());
    assertEquals(2, request.getPage());
    assertEquals("desc", request.getSort());
    assertEquals(20, request.getPageSize());
    assertEquals(filters, request.getFilters());
  }

  @Test
  void testToString() {
    // Test for @Data (toString)
    Map<String, String> filters = new HashMap<>();
    filters.put("category", "Fiction");

    SearchRequest request = new SearchRequest("Java", 1, "asc", 10, filters);
    String requestString = request.toString();

    assertThat(requestString)
        .contains("keyword=Java")
        .contains("page=1")
        .contains("sort=asc")
        .contains("pageSize=10")
        .contains("category=Fiction");
  }

  @Test
  void testEqualsAndHashCode() {
    // Test for @Data (equals and hashCode)
    Map<String, String> filters1 = new HashMap<>();
    filters1.put("category", "Fiction");

    Map<String, String> filters2 = new HashMap<>();
    filters2.put("category", "Fiction");

    SearchRequest request1 = new SearchRequest("Java", 1, "asc", 10, filters1);
    SearchRequest request2 = new SearchRequest("Java", 1, "asc", 10, filters2);

    assertEquals(request1, request2);
    assertEquals(request1.hashCode(), request2.hashCode());
  }
}