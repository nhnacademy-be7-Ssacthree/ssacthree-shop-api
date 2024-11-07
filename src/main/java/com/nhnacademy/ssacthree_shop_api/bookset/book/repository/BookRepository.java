package com.nhnacademy.ssacthree_shop_api.bookset.book.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, BookCustomRepository {

    // 도서 고유 번호로 도서 찾기
    Book findByBookIsbn(String isbn);

    // 도서 재고 검색
    Integer findStockByBookId(Long bookId);

    // 도서 조회수 검색
    Integer findBookViewCountByBookId(Long bookId);

}
