package com.nhnacademy.ssacthree_shop_api.elastic.exception;

public class ElasticsearchOperationException extends RuntimeException {
  public ElasticsearchOperationException(String message) {
    super(message);
  }
}
