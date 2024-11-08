package com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;

import java.util.List;

public interface BookAuthorCustomRepository {
    // 책 id로 책의 작가 이름 조회
    List<Author> findBookAuthorByBookId(Long bookId);
}
