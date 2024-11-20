package com.nhnacademy.ssacthree_shop_api.bookset.book.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long>, BookCustomRepository,
    BookMgmtRepository {

    // 도서 재고 검색
    Integer findStockByBookId(Long bookId);

    // 도서 조회수 검색
    Integer findBookViewCountByBookId(Long bookId);

    Optional<Book> findBookByBookIsbn(String bookIsbn);

    Optional<Book> findByBookId(Long bookId);


}
