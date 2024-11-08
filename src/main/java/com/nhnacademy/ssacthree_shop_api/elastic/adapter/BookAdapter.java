package com.nhnacademy.ssacthree_shop_api.elastic.adapter;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;

/*
  Book 을 BookDocument로 변환 해주는 클래스

 */


public class BookAdapter {
  public static BookDocument toDocument(Book book) {
    return new BookDocument(
        book.getBookId(),              // 도서 ID
        book.getBookName(),            // 도서 제목
        book.getBookInfo(),            // 도서 설명
        book.getBookIsbn(),            // ISBN
        book.getPublicationDate(),     // 출판일
        book.getSalePrice(),           // 할인 가격
        book.getBookDiscount()         // 할인율
    );
  }
}
