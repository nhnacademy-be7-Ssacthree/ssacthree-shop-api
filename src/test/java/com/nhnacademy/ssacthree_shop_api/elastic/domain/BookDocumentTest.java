package com.nhnacademy.ssacthree_shop_api.elastic.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookDocumentTest {

  @Test
  void testAllArgsConstructor() {
    // Arrange
    long bookId = 1L;
    String bookName = "Effective Java";
    String bookIndex = "effective-java-index";
    String bookInfo = "A comprehensive guide to best practices in Java.";
    String bookIsbn = "9780134685991";
    String publicationDate = "2018-12-27";
    int regularPrice = 50000;
    int salePrice = 45000;
    boolean isPacked = true;
    int stock = 100;
    String bookThumbnailImageUrl = "http://example.com/effective-java.jpg";
    int bookViewCount = 500;
    int bookDiscount = 10;
    String publisherNames = "Addison-Wesley";
    String authorNames = "Joshua Bloch";
    List<String> tagNames = List.of("Java", "Programming", "Best Practices");
    List<String> category = List.of("Technology", "Software Development");

    // Act
    BookDocument bookDocument = new BookDocument(
        bookId, bookName, bookIndex, bookInfo, bookIsbn, publicationDate,
        regularPrice, salePrice, isPacked, stock, bookThumbnailImageUrl,
        bookViewCount, bookDiscount, publisherNames, authorNames,
        tagNames, category
    );

    // Assert
    assertEquals(bookId, bookDocument.getBookId());
    assertEquals(bookName, bookDocument.getBookName());
    assertEquals(bookIndex, bookDocument.getBookIndex());
    assertEquals(bookInfo, bookDocument.getBookInfo());
    assertEquals(bookIsbn, bookDocument.getBookIsbn());
    assertEquals(publicationDate, bookDocument.getPublicationDate());
    assertEquals(regularPrice, bookDocument.getRegularPrice());
    assertEquals(salePrice, bookDocument.getSalePrice());
    assertTrue(bookDocument.isPacked());
    assertEquals(stock, bookDocument.getStock());
    assertEquals(bookThumbnailImageUrl, bookDocument.getBookThumbnailImageUrl());
    assertEquals(bookViewCount, bookDocument.getBookViewCount());
    assertEquals(bookDiscount, bookDocument.getBookDiscount());
    assertEquals(publisherNames, bookDocument.getPublisherNames());
    assertEquals(authorNames, bookDocument.getAuthorNames());
    assertEquals(tagNames, bookDocument.getTagNames());
    assertEquals(category, bookDocument.getCategory());
  }

  @Test
  void testNoArgsConstructorAndSetters() {
    // Arrange
    BookDocument bookDocument = new BookDocument();

    // Act
    bookDocument.setBookId(2L);
    bookDocument.setBookName("Clean Code");
    bookDocument.setBookIndex("clean-code-index");
    bookDocument.setBookInfo("A Handbook of Agile Software Craftsmanship.");
    bookDocument.setBookIsbn("9780132350884");
    bookDocument.setPublicationDate("2008-08-01");
    bookDocument.setRegularPrice(60000);
    bookDocument.setSalePrice(54000);
    bookDocument.setPacked(false);
    bookDocument.setStock(50);
    bookDocument.setBookThumbnailImageUrl("http://example.com/clean-code.jpg");
    bookDocument.setBookViewCount(1000);
    bookDocument.setBookDiscount(20);
    bookDocument.setPublisherNames("Prentice Hall");
    bookDocument.setAuthorNames("Robert C. Martin");
    bookDocument.setTagNames(List.of("Programming", "Agile", "Clean Code"));
    bookDocument.setCategory(List.of("Software Development", "Technology"));

    // Assert
    assertEquals(2L, bookDocument.getBookId());
    assertEquals("Clean Code", bookDocument.getBookName());
    assertEquals("clean-code-index", bookDocument.getBookIndex());
    assertEquals("A Handbook of Agile Software Craftsmanship.", bookDocument.getBookInfo());
    assertEquals("9780132350884", bookDocument.getBookIsbn());
    assertEquals("2008-08-01", bookDocument.getPublicationDate());
    assertEquals(60000, bookDocument.getRegularPrice());
    assertEquals(54000, bookDocument.getSalePrice());
    assertFalse(bookDocument.isPacked());
    assertEquals(50, bookDocument.getStock());
    assertEquals("http://example.com/clean-code.jpg", bookDocument.getBookThumbnailImageUrl());
    assertEquals(1000, bookDocument.getBookViewCount());
    assertEquals(20, bookDocument.getBookDiscount());
    assertEquals("Prentice Hall", bookDocument.getPublisherNames());
    assertEquals("Robert C. Martin", bookDocument.getAuthorNames());
    assertEquals(List.of("Programming", "Agile", "Clean Code"), bookDocument.getTagNames());
    assertEquals(List.of("Software Development", "Technology"), bookDocument.getCategory());
  }
}
