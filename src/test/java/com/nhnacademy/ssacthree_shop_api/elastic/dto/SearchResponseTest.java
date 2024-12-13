package com.nhnacademy.ssacthree_shop_api.elastic.dto;

import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SearchResponseTest {

  @Test
  void testNoArgsConstructor() {
    // Test for @NoArgsConstructor
    SearchResponse response = new SearchResponse();
    assertNotNull(response);
    assertThat(response.getTotalHits()).isNull();
    assertThat(response.getBooks()).isNull();
  }

  @Test
  void testAllArgsConstructor() {
    // Test for @AllArgsConstructor
    List<BookDocument> books = new ArrayList<>();
    books.add(new BookDocument(1L, "Book Title", "Index", "Info", "123456789", "2023-01-01", 20000, 18000, true, 10, "url", 100, 10, "Publisher", "Author", List.of("Tag1", "Tag2"), List.of("Category1")));

    SearchResponse response = new SearchResponse(10, books);

    assertNotNull(response);
    assertEquals(10, response.getTotalHits());
    assertEquals(books, response.getBooks());
  }

  @Test
  void testGetterAndSetter() {
    // Test for @Data (Getter and Setter)
    SearchResponse response = new SearchResponse();
    List<BookDocument> books = new ArrayList<>();
    books.add(new BookDocument(1L, "Book Title", "Index", "Info", "123456789", "2023-01-01", 20000, 18000, true, 10, "url", 100, 10, "Publisher", "Author", List.of("Tag1", "Tag2"), List.of("Category1")));

    response.setTotalHits(5);
    response.setBooks(books);

    assertEquals(5, response.getTotalHits());
    assertEquals(books, response.getBooks());
  }

  @Test
  void testToString() {
    // Test for @Data (toString)
    List<BookDocument> books = new ArrayList<>();
    books.add(new BookDocument(1L, "Book Title", "Index", "Info", "123456789", "2023-01-01", 20000, 18000, true, 10, "url", 100, 10, "Publisher", "Author", List.of("Tag1", "Tag2"), List.of("Category1")));

    SearchResponse response = new SearchResponse(10, books);
    String responseString = response.toString();

    assertThat(responseString)
        .contains("totalHits=10")
        .contains("Book Title")
        .contains("Publisher");
  }

  @Test
  void testEqualsAndHashCode() {
    // Test for @Data (equals and hashCode)
    List<BookDocument> books1 = new ArrayList<>();
    books1.add(new BookDocument(1L, "Book Title", "Index", "Info", "123456789", "2023-01-01", 20000, 18000, true, 10, "url", 100, 10, "Publisher", "Author", List.of("Tag1", "Tag2"), List.of("Category1")));

    List<BookDocument> books2 = new ArrayList<>();
    books2.add(new BookDocument(1L, "Book Title", "Index", "Info", "123456789", "2023-01-01", 20000, 18000, true, 10, "url", 100, 10, "Publisher", "Author", List.of("Tag1", "Tag2"), List.of("Category1")));

    SearchResponse response1 = new SearchResponse(10, books1);
    SearchResponse response2 = new SearchResponse(10, books2);

    assertEquals(response1, response2);
    assertEquals(response1.hashCode(), response2.hashCode());
  }
}