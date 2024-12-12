package com.nhnacademy.ssacthree_shop_api.bookset.discount.domain;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DiscountTest {

  @Mock
  private Book mockBook;

  private Discount mockDiscount;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this); // Initialize the mocks

    // Initialize the mockBook with necessary fields
    when(mockBook.getBookId()).thenReturn(1L);
    when(mockBook.getBookName()).thenReturn("Book1");

    // Initialize the Discount entity
    mockDiscount = new Discount(
        1L, // Discount ID (can be set to any value since it's not auto-generated here)
        10, // Book discount log
        LocalDateTime.now(), // Book discount date
        mockBook // Use the mocked Book entity
    );
  }

  @Test
  public void testDiscountCreation() {
    // Test that the Discount entity is created correctly
    assertEquals(10, mockDiscount.getBookDiscountLog()); // Check if discount log is set correctly
    assertEquals(mockBook, mockDiscount.getBook()); // Check if Book entity is correctly assigned
    assertEquals("Book1", mockDiscount.getBook().getBookName()); // Check if the book's title matches
  }

  @Test
  public void testBookDiscountDate() {
    // Test that the Discount's discount date is set correctly
    LocalDateTime discountDate = mockDiscount.getBookDiscountDate();
    assertEquals(LocalDateTime.now().getDayOfYear(), discountDate.getDayOfYear()); // Check if the date is set properly (ignoring time comparison)
  }
}
