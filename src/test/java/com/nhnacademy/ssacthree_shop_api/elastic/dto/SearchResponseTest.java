package com.nhnacademy.ssacthree_shop_api.elastic.dto;

import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SearchResponseTest {

  private SearchResponse searchResponse;
  private List<BookDocument> bookDocuments;

  @BeforeEach
  void setUp() {
    bookDocuments = new ArrayList<>();
    bookDocuments.add(new BookDocument(
        1L,
        "Effective Java",
        "index1",
        "A programming guide for Java developers",
        "1234567890",
        "2008-05-08",
        50000,
        45000,
        false,
        100,
        "image_url",
        200,
        10,
        "Publisher Name",
        "Author Name",
        List.of("tag1", "tag2"),
        List.of("category1", "category2")
    ));

    searchResponse = new SearchResponse(
        1,          // totalHits
        bookDocuments // books
    );
  }

  @Test
  void testSearchResponseInitialization() {
    assertThat(searchResponse.getTotalHits()).isEqualTo(1);
    assertThat(searchResponse.getBooks()).isEqualTo(bookDocuments);
  }

  @Test
  void testSettersAndGetters() {
    searchResponse.setTotalHits(2);

    List<BookDocument> newBookDocuments = new ArrayList<>();
    newBookDocuments.add(new BookDocument(
        2L,
        "Clean Code",
        "index2",
        "A handbook of agile software craftsmanship",
        "0987654321",
        "2010-10-10",
        55000,
        50000,
        true,
        50,
        "new_image_url",
        150,
        15,
        "New Publisher",
        "New Author",
        List.of("tag3", "tag4"),
        List.of("category3", "category4")
    ));
    searchResponse.setBooks(newBookDocuments);

    assertThat(searchResponse.getTotalHits()).isEqualTo(2);
    assertThat(searchResponse.getBooks()).isEqualTo(newBookDocuments);
  }

  @Test
  void testEmptyBooks() {
    searchResponse.setBooks(new ArrayList<>());
    assertThat(searchResponse.getBooks()).isEmpty();
  }
}
