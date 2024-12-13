package com.nhnacademy.ssacthree_shop_api.bookset.author.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorCustomRepository {
    Page<AuthorGetResponse> findAllAuthors(Pageable pageable);

    List<AuthorGetResponse> findAllAuthorList();

    AuthorGetResponse findAuthorById(Long authorId);
}
