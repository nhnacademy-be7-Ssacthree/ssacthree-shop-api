package com.nhnacademy.ssacthree_shop_api.bookset.author.service;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorUpdateRequest;

import java.util.List;

public interface AuthorService {

    Author createAuthor(AuthorCreateRequest authorCreateRequest);

    Author updateAuthor(AuthorUpdateRequest authorUpdateRequest);

    List<AuthorGetResponse> getAllAuthors();

    AuthorGetResponse getAuthorById(long authorId);

    void deleteAuthor(long authorId);
}
