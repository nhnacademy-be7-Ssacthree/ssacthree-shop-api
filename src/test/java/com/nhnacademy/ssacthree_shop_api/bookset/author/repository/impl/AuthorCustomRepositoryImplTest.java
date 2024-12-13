package com.nhnacademy.ssacthree_shop_api.bookset.author.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.QAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorGetResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class AuthorCustomRepositoryImplTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @InjectMocks
    private AuthorCustomRepositoryImpl authorCustomRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllAuthors_ShouldReturnPagedResults() {
        // Given
        PageRequest pageable = PageRequest.of(0, 2); // 페이징 설정
        AuthorGetResponse response = new AuthorGetResponse(1L, "Author1", "Info1");
        List<AuthorGetResponse> mockResult = Collections.singletonList(response);

        JPAQuery<AuthorGetResponse> mockQuery = mock(JPAQuery.class);

        when(queryFactory.select(
            Projections.constructor(
                AuthorGetResponse.class,
                QAuthor.author.authorId,
                QAuthor.author.authorName,
                QAuthor.author.authorInfo
            )
        )).thenReturn(mockQuery);

        when(mockQuery.from(QAuthor.author)).thenReturn(mockQuery);
        when(mockQuery.offset(pageable.getOffset())).thenReturn(mockQuery);
        when(mockQuery.limit(pageable.getPageSize())).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(mockResult);

        Long mockCount = 1L;
        JPAQuery<Long> countQuery = mock(JPAQuery.class);

        when(queryFactory.select(QAuthor.author.count())).thenReturn(countQuery);
        when(countQuery.from(QAuthor.author)).thenReturn(countQuery);
        when(countQuery.fetchOne()).thenReturn(mockCount);

        // When
        Page<AuthorGetResponse> result = authorCustomRepository.findAllAuthors(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getAuthorName()).isEqualTo("Author1");
        assertThat(result.getTotalElements()).isEqualTo(mockCount);

        verify(queryFactory).select(
            Projections.constructor(
                AuthorGetResponse.class,
                QAuthor.author.authorId,
                QAuthor.author.authorName,
                QAuthor.author.authorInfo
            )
        );
        verify(mockQuery).from(QAuthor.author);
        verify(mockQuery).offset(pageable.getOffset());
        verify(mockQuery).limit(pageable.getPageSize());
        verify(mockQuery).fetch();
        verify(queryFactory).select(QAuthor.author.count());
        verify(countQuery).from(QAuthor.author);
        verify(countQuery).fetchOne();
    }

    @Test
    void findAuthorById_ShouldReturnSpecificAuthor() {
        // Given
        Long authorId = 1L;
        AuthorGetResponse response = new AuthorGetResponse(1L, "Author1", "Info1");

        JPAQuery<AuthorGetResponse> mockQuery = mock(JPAQuery.class);

        when(queryFactory.select(
            Projections.constructor(
                AuthorGetResponse.class,
                QAuthor.author.authorId,
                QAuthor.author.authorName,
                QAuthor.author.authorInfo
            )
        )).thenReturn(mockQuery);

        when(mockQuery.from(QAuthor.author)).thenReturn(mockQuery);
        when(mockQuery.where(QAuthor.author.authorId.eq(authorId))).thenReturn(mockQuery);
        when(mockQuery.fetchOne()).thenReturn(response);

        // When
        AuthorGetResponse result = authorCustomRepository.findAuthorById(authorId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAuthorId()).isEqualTo(1L);
        assertThat(result.getAuthorName()).isEqualTo("Author1");

        verify(queryFactory).select(
            Projections.constructor(
                AuthorGetResponse.class,
                QAuthor.author.authorId,
                QAuthor.author.authorName,
                QAuthor.author.authorInfo
            )
        );
        verify(mockQuery).from(QAuthor.author);
        verify(mockQuery).where(QAuthor.author.authorId.eq(authorId));
        verify(mockQuery).fetchOne();
    }


    @Test
    void findAllAuthorList_ShouldReturnAllAuthors() {
        // Given
        AuthorGetResponse response1 = new AuthorGetResponse(1L, "Author1", "Info1");
        AuthorGetResponse response2 = new AuthorGetResponse(2L, "Author2", "Info2");
        List<AuthorGetResponse> mockResult = List.of(response1, response2);

        JPAQuery<AuthorGetResponse> mockQuery = mock(JPAQuery.class);

        when(queryFactory.select(
            Projections.constructor(
                AuthorGetResponse.class,
                QAuthor.author.authorId,
                QAuthor.author.authorName,
                QAuthor.author.authorInfo
            )
        )).thenReturn(mockQuery);

        when(mockQuery.from(QAuthor.author)).thenReturn(mockQuery);
        when(mockQuery.orderBy(QAuthor.author.authorId.asc())).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(mockResult);

        // When
        List<AuthorGetResponse> result = authorCustomRepository.findAllAuthorList();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getAuthorName()).isEqualTo("Author1");
        assertThat(result.get(1).getAuthorName()).isEqualTo("Author2");

        verify(queryFactory).select(
            Projections.constructor(
                AuthorGetResponse.class,
                QAuthor.author.authorId,
                QAuthor.author.authorName,
                QAuthor.author.authorInfo
            )
        );
        verify(mockQuery).from(QAuthor.author);
        verify(mockQuery).orderBy(QAuthor.author.authorId.asc());
        verify(mockQuery).fetch();
    }

}
