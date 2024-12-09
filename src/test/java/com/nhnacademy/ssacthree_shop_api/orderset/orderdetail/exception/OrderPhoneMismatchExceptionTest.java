package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception;

import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception.OrderPhoneMismatchException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class OrderPhoneMismatchExceptionTest {

  @Test
  void testOrderPhoneMismatchExceptionConstructor() {
    // Given
    String phone = "123-456-7890";

    // Create the exception instance
    OrderPhoneMismatchException exception = new OrderPhoneMismatchException(phone);

    // Verify the exception message
    assertEquals("일치하지 않는 전화번호: 123-456-7890", exception.getMessage());

    // Verify the HttpStatus
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());  // Verifying the status code is BAD_REQUEST (400)
  }

  @Test
  void testOrderPhoneMismatchExceptionWithNullPhone() {
    // Given
    String phone = null;

    // Create the exception instance
    OrderPhoneMismatchException exception = new OrderPhoneMismatchException(phone);

    // Verify the exception message
    assertEquals("일치하지 않는 전화번호: null", exception.getMessage());

    // Verify the HttpStatus
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());  // Verifying the status code is BAD_REQUEST (400)
  }
}

