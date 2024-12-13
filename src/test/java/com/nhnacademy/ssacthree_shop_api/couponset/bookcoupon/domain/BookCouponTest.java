package com.nhnacademy.ssacthree_shop_api.couponset.bookcoupon.domain;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookCouponTest {

  private Coupon coupon;
  private Book book;
  private BookCoupon bookCoupon;

  @BeforeEach
  void setUp() {
    // Coupon과 Book 객체를 Mocking
    coupon = mock(Coupon.class);
    book = mock(Book.class);

    // BookCoupon 객체 생성
    bookCoupon = new BookCoupon(coupon, book);
  }

  @Test
  void testBookCouponCreation() {
    // 객체 생성 검증
    assertNotNull(bookCoupon);
    assertEquals(coupon, bookCoupon.getCoupon());
    assertEquals(book, bookCoupon.getBook());
  }

  @Test
  void testCouponAndBookSetters() {
    // 새로운 Coupon과 Book 객체를 설정
    Coupon newCoupon = mock(Coupon.class);
    Book newBook = mock(Book.class);

    bookCoupon = new BookCoupon(newCoupon, newBook);

    // Setter 메서드를 통해 값이 변경된 경우 검증
    assertEquals(newCoupon, bookCoupon.getCoupon());
    assertEquals(newBook, bookCoupon.getBook());
  }

  @Test
  void testNullCoupon() {
    // Coupon 객체를 null로 설정하고 BookCoupon 객체 생성
    bookCoupon = new BookCoupon(null, book);

    // Coupon이 null인 경우 검증
    assertNull(bookCoupon.getCoupon());
    assertEquals(book, bookCoupon.getBook());
  }

  @Test
  void testNullBook() {
    // Book 객체를 null로 설정하고 BookCoupon 객체 생성
    bookCoupon = new BookCoupon(coupon, null);

    // Book이 null인 경우 검증
    assertEquals(coupon, bookCoupon.getCoupon());
    assertNull(bookCoupon.getBook());
  }
}
