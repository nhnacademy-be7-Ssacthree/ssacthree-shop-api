package com.nhnacademy.ssacthree_shop_api.bookset.author.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.QAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.author.exception.AuthorNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.author.repository.AuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.author.service.AuthorService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {
    private final static String AUTHOR_CREATE_ERROR_MESSAGE = "작가 정보 생성에 실패했습니다.";
    private final static String AUTHOR_ID_ERROR_MESSAGE = "authorId는 1보다 작을 수 없습니다.";
    private final static String AUTHOR_NOT_FOUND_MESSAGE = "해당 아이디를 찾을 수 없습니다.:";
    @PersistenceContext
    private EntityManager entityManager;

    private final AuthorRepository authorRepository;

    @Override
    public Author createAuthor(AuthorCreateRequest authorCreateRequest) {
        Author author = new Author(
                authorCreateRequest.getAuthorName(),
                authorCreateRequest.getAuthorInfo()
        );

        if(getAllAuthors().isEmpty()){
            throw new AuthorNotFoundException(AUTHOR_CREATE_ERROR_MESSAGE);
        }

        return authorRepository.save(author);
    }

    @Override
    public Author updateAuthor(AuthorUpdateRequest authorUpdateRequest) {
        long authorId = authorUpdateRequest.getAuthorId();
        if(authorId < 1){
            throw new AuthorNotFoundException(AUTHOR_ID_ERROR_MESSAGE);
        }
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(AUTHOR_NOT_FOUND_MESSAGE + authorId));

        author.setAuthorName(authorUpdateRequest.getAuthorName());
        author.setAuthorInfo(authorUpdateRequest.getAuthorInfo());

        return authorRepository.save(author);
    }

    @Override
    public List<AuthorGetResponse> getAllAuthors() {
        QAuthor qAuthor = QAuthor.author;

        return new JPAQueryFactory(entityManager)
                .select(Projections.constructor(
                        AuthorGetResponse.class,
                        qAuthor.authorId,
                        qAuthor.authorName,
                        qAuthor.authorInfo
                ))
                .from(qAuthor)
                .orderBy(qAuthor.authorId.asc())
                .fetch();
    }
}
