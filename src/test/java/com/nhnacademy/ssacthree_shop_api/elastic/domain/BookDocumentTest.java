package com.nhnacademy.ssacthree_shop_api.elastic.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BookDocumentTest {

  @Test
  void testNoArgsConstructor() {
    // Test for @NoArgsConstructor
    BookDocument bookDocument = new BookDocument();
    assertNotNull(bookDocument);
    assertThat(bookDocument.getBookId()).isEqualTo(0);
    assertThat(bookDocument.getBookName()).isNull();
  }

  @Test
  void testAllArgsConstructor() {
    // Test for @AllArgsConstructor
    List<String> tags = Arrays.asList("Technology", "Programming");
    List<String> categories = Arrays.asList("Fiction", "Education");

    BookDocument bookDocument = new BookDocument(
        1L, "Effective Java", "Index1", "A Java programming book", "1234567890",
        "2023-01-01", 45000, 40000, true, 100, "image.jpg",
        500, 10, "NHN Publisher", "Joshua Bloch", tags, categories
    );

    assertNotNull(bookDocument);
    assertEquals(1L, bookDocument.getBookId());
    assertEquals("Effective Java", bookDocument.getBookName());
    assertEquals(tags, bookDocument.getTagNames());
    assertEquals(categories, bookDocument.getCategory());
  }

  @Test
  void testGetterAndSetter() {
    // Test for @Data (Getter and Setter)
    BookDocument bookDocument = new BookDocument();
    bookDocument.setBookId(2L);
    bookDocument.setBookName("Clean Code");

    assertEquals(2L, bookDocument.getBookId());
    assertEquals("Clean Code", bookDocument.getBookName());
  }

  @Test
  void testToString() {
    // Test for @Data (toString)
    List<String> tags = Arrays.asList("Technology", "Programming");
    BookDocument bookDocument = new BookDocument(
        1L, "Effective Java", "Index1", "A Java programming book", "1234567890",
        "2023-01-01", 45000, 40000, true, 100, "image.jpg",
        500, 10, "NHN Publisher", "Joshua Bloch", tags, null
    );

    String documentString = bookDocument.toString();
    assertThat(documentString)
        .contains("bookId=1")
        .contains("bookName=Effective Java")
        .contains("tagNames=[Technology, Programming]");
  }

  @Test
  void testEqualsAndHashCode() {
    // Test for @Data (equals and hashCode)
    List<String> tags1 = Arrays.asList("Technology", "Programming");
    List<String> tags2 = Arrays.asList("Technology", "Programming");

    BookDocument book1 = new BookDocument(
        1L, "Effective Java", "Index1", "A Java programming book", "1234567890",
        "2023-01-01", 45000, 40000, true, 100, "image.jpg",
        500, 10, "NHN Publisher", "Joshua Bloch", tags1, null
    );

    BookDocument book2 = new BookDocument(
        1L, "Effective Java", "Index1", "A Java programming book", "1234567890",
        "2023-01-01", 45000, 40000, true, 100, "image.jpg",
        500, 10, "NHN Publisher", "Joshua Bloch", tags2, null
    );

    assertEquals(book1, book2);
    assertEquals(book1.hashCode(), book2.hashCode());
  }
}