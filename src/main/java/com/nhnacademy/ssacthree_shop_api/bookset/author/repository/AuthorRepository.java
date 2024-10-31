package com.nhnacademy.ssacthree_shop_api.bookset.author.repository;

import java.util.List;
import java.util.Optional;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findAllByAuthorNameIn(List<String> authorNames);
    Optional<Author> findByAuthorName(String authorName);
}
