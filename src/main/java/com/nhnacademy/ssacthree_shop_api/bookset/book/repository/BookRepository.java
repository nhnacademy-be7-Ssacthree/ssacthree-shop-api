package com.nhnacademy.ssacthree_shop_api.bookset.book.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    // 도서 아이디로 도서 찾기
    // 제목으로 도서 찾기
    // 도서 고유 번호로 도서 찾기
    // 도서 아이디로 정가 검색
    // 도서 아이디로 판매가 검색
    // 도서 아이디로 포장 가능 여부 판단
    // 도서 아이디로 재고 판단
    // 도서 아이디로 썸네일 이미지 검색
    // 도서 아이디로 도서 조회수 검색
    //
}
