package com.nhnacademy.ssacthree_shop_api.bookset.book.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CsvProcessingExceptionTest {

  @Test
  void testCsvProcessingExceptionWithMessage() {
    // Given
    String expectedMessage = "CSV processing failed";

    // Create the CsvProcessingException instance with a message
    CsvProcessingException exception = new CsvProcessingException(expectedMessage);

    // Verify the message is set correctly
    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  void testCsvProcessingExceptionWithMessageAndCause() {
    // Given
    String expectedMessage = "CSV processing failed";
    Throwable cause = new RuntimeException("Cause of the exception");

    // Create the CsvProcessingException instance with a message and cause
    CsvProcessingException exception = new CsvProcessingException(expectedMessage, cause);

    // Verify the message is set correctly
    assertEquals(expectedMessage, exception.getMessage());

    // Verify the cause is set correctly
    assertEquals(cause, exception.getCause());
  }
}
