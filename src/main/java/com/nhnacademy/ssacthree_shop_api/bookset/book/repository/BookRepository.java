package com.nhnacademy.ssacthree_shop_api.bookset.book.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
