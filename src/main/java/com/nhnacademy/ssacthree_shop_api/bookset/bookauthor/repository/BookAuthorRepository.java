package com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {
    //todo: custom repository에서 구현해야 할듯
    // 책 id로 책의 작가 이름 조회
    List<Author> findBookAuthorByBookId(Long bookId);
}
