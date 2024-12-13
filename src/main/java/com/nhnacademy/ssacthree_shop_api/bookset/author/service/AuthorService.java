package com.nhnacademy.ssacthree_shop_api.bookset.author.service;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorService {

    Author createAuthor(AuthorCreateRequest authorCreateRequest);

    Author updateAuthor(AuthorUpdateRequest authorUpdateRequest);

    Page<AuthorGetResponse> getAllAuthors(Pageable pageable);

    List<AuthorGetResponse> getAllAuthorList();

    AuthorGetResponse getAuthorById(Long authorId);

    void deleteAuthor(Long authorId);
}
