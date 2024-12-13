package com.nhnacademy.ssacthree_shop_api.bookset.author.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.QAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.author.repository.AuthorCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuthorCustomRepositoryImpl implements AuthorCustomRepository {
    private final JPAQueryFactory queryFactory;

    private static final QAuthor author = QAuthor.author;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<AuthorGetResponse> findAllAuthors(Pageable pageable) {
        List<AuthorGetResponse> authors = queryFactory.select(Projections.constructor(
                        AuthorGetResponse.class,
                        author.authorId,
                        author.authorName,
                        author.authorInfo))
                        .from(author)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        Long count = queryFactory.select(author.count())
                .from(author)
                .fetchOne();

        count = (count == null) ? 0 : count;

        return new PageImpl<>(authors, pageable, count);
    }

    @Override
    public List<AuthorGetResponse> findAllAuthorList() {
        return queryFactory.select(Projections.constructor(
                AuthorGetResponse.class,
                author.authorId,
                author.authorName,
                author.authorInfo
            ))
            .from(author)
            .orderBy(author.authorId.asc())
            .fetch();
    }

    @Override
    public AuthorGetResponse findAuthorById(Long authorId) {
        return queryFactory.select(Projections.constructor(
                    AuthorGetResponse.class,
                    author.authorId,
                    author.authorName,
                    author.authorInfo
                ))
                .from(author)
                .where(author.authorId.eq(authorId))
                .fetchOne();
    }

}
