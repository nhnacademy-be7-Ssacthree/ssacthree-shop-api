package com.nhnacademy.ssacthree_shop_api.bookset.tag.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.QTag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
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
import org.springframework.data.domain.Pageable;

class TagCustomRepositoryImplTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @InjectMocks
    private TagCustomRepositoryImpl tagCustomRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllTags_ShouldReturnPagedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 2); // 페이징 설정
        TagInfoResponse response = new TagInfoResponse(1L, "Tag1");
        List<TagInfoResponse> mockResult = Collections.singletonList(response);

        JPAQuery<TagInfoResponse> mockQuery = mock(JPAQuery.class);

        when(queryFactory.select(
            Projections.constructor(
                TagInfoResponse.class,
                QTag.tag.tagId,
                QTag.tag.tagName
            )
        )).thenReturn(mockQuery);

        when(mockQuery.from(QTag.tag)).thenReturn(mockQuery);
        when(mockQuery.offset(pageable.getOffset())).thenReturn(mockQuery);
        when(mockQuery.limit(pageable.getPageSize())).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(mockResult);

        Long mockCount = 1L;
        JPAQuery<Long> countQuery = mock(JPAQuery.class);

        when(queryFactory.select(QTag.tag.count())).thenReturn(countQuery);
        when(countQuery.from(QTag.tag)).thenReturn(countQuery);
        when(countQuery.fetchOne()).thenReturn(mockCount);

        // When
        Page<TagInfoResponse> result = tagCustomRepository.findAllTags(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTagName()).isEqualTo("Tag1");
        assertThat(result.getTotalElements()).isEqualTo(mockCount);

        verify(queryFactory).select(
            Projections.constructor(
                TagInfoResponse.class,
                QTag.tag.tagId,
                QTag.tag.tagName
            )
        );
        verify(mockQuery).from(QTag.tag);
        verify(mockQuery).offset(pageable.getOffset());
        verify(mockQuery).limit(pageable.getPageSize());
        verify(mockQuery).fetch();
        verify(queryFactory).select(QTag.tag.count());
        verify(countQuery).from(QTag.tag);
        verify(countQuery).fetchOne();
    }

    @Test
    void findAllTagList_ShouldReturnAllTags() {
        // Given
        TagInfoResponse response1 = new TagInfoResponse(1L, "Tag1");
        TagInfoResponse response2 = new TagInfoResponse(2L, "Tag2");
        List<TagInfoResponse> mockResult = List.of(response1, response2);

        JPAQuery<TagInfoResponse> mockQuery = mock(JPAQuery.class);

        when(queryFactory.select(
            Projections.constructor(
                TagInfoResponse.class,
                QTag.tag.tagId,
                QTag.tag.tagName
            )
        )).thenReturn(mockQuery);

        when(mockQuery.from(QTag.tag)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(mockResult);

        // When
        List<TagInfoResponse> result = tagCustomRepository.findAllTagList();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTagName()).isEqualTo("Tag1");
        assertThat(result.get(1).getTagName()).isEqualTo("Tag2");

        verify(queryFactory).select(
            Projections.constructor(
                TagInfoResponse.class,
                QTag.tag.tagId,
                QTag.tag.tagName
            )
        );
        verify(mockQuery).from(QTag.tag);
        verify(mockQuery).fetch();
    }
}
