package com.nhnacademy.ssacthree_shop_api.elastic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SearchRequestTest {

  private SearchRequest searchRequest;

  @BeforeEach
  void setUp() {
    Map<String, String> filters = new HashMap<>();
    filters.put("category", "fiction");
    filters.put("tag", "bestseller");

    searchRequest = new SearchRequest(
        "Java Programming", // keyword
        1,                  // page
        "price",            // sort
        10,                 // pageSize
        filters              // filters
    );
  }

  @Test
  void testSearchRequestInitialization() {
    assertThat(searchRequest.getKeyword()).isEqualTo("Java Programming");
    assertThat(searchRequest.getPage()).isEqualTo(1);
    assertThat(searchRequest.getSort()).isEqualTo("price");
    assertThat(searchRequest.getPageSize()).isEqualTo(10);
    assertThat(searchRequest.getFilters()).containsEntry("category", "fiction")
        .containsEntry("tag", "bestseller");
  }

  @Test
  void testSettersAndGetters() {
    searchRequest.setKeyword("Python Programming");
    searchRequest.setPage(2);
    searchRequest.setSort("relevance");
    searchRequest.setPageSize(20);

    Map<String, String> newFilters = new HashMap<>();
    newFilters.put("category", "non-fiction");
    newFilters.put("tag", "editor-choice");
    searchRequest.setFilters(newFilters);

    assertThat(searchRequest.getKeyword()).isEqualTo("Python Programming");
    assertThat(searchRequest.getPage()).isEqualTo(2);
    assertThat(searchRequest.getSort()).isEqualTo("relevance");
    assertThat(searchRequest.getPageSize()).isEqualTo(20);
    assertThat(searchRequest.getFilters()).containsEntry("category", "non-fiction")
        .containsEntry("tag", "editor-choice");
  }

  @Test
  void testEmptyFilters() {
    searchRequest.setFilters(new HashMap<>());
    assertThat(searchRequest.getFilters()).isEmpty();
  }
}
