package com.nhnacademy.ssacthree_shop_api.elastic.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElasticsearchOperationExceptionTest {

  @Test
  void testElasticsearchOperationExceptionWithMessage() {
    // Given
    String expectedMessage = "Elasticsearch operation failed";

    // Create the ElasticsearchOperationException instance with the message
    ElasticsearchOperationException exception = new ElasticsearchOperationException(expectedMessage);

    // Verify the message is set correctly
    assertEquals(expectedMessage, exception.getMessage());
  }
}
