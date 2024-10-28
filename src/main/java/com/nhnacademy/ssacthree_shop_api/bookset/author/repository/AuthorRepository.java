package com.nhnacademy.ssacthree_shop_api.bookset.author.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
