package com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.QPublisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherGetResponse;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublisherCustomRepositoryImplTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAQuery<PublisherGetResponse> mockedQuery;

    @Mock
    private JPAQuery<Long> mockedCountQuery;

    @InjectMocks
    private PublisherCustomRepositoryImpl publisherCustomRepository;

    private static final QPublisher publisher = QPublisher.publisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllPublisher() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 2);

        List<PublisherGetResponse> mockPublishers = Arrays.asList(
                new PublisherGetResponse(1L, "Publisher 1", true),
                new PublisherGetResponse(2L, "Publisher 2", false)
        );

        // 데이터 페이징용 쿼리 설정
        when(queryFactory.select(any(Expression.class)))
                .thenAnswer(invocation -> {
                    Expression<?> expression = invocation.getArgument(0);
                    if (expression.equals(Projections.constructor(
                            PublisherGetResponse.class,
                            publisher.publisherId,
                            publisher.publisherName,
                            publisher.publisherIsUsed))) {
                        return mockedQuery;
                    } else if (expression.equals(publisher.count())) {
                        return mockedCountQuery;
                    }
                    return null;
                });

        when(mockedQuery.from(publisher)).thenReturn(mockedQuery);
        when(mockedQuery.offset(pageable.getOffset())).thenReturn(mockedQuery);
        when(mockedQuery.limit(pageable.getPageSize())).thenReturn(mockedQuery);
        when(mockedQuery.fetch()).thenReturn(mockPublishers);

        // 카운트용 쿼리 설정
        when(mockedCountQuery.from(publisher)).thenReturn(mockedCountQuery);
        when(mockedCountQuery.fetchOne()).thenReturn(2L);

        // Act
        Page<PublisherGetResponse> result = publisherCustomRepository.findAllPublisher(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        PublisherGetResponse publisher1 = result.getContent().get(0);
        assertEquals("Publisher 1", publisher1.getPublisherName());
        assertTrue(publisher1.isPublisherIsUsed());

        // 각 메서드 호출 검증
        verify(queryFactory, times(2)).select(any(Expression.class));
        verify(mockedQuery, times(1)).from(publisher);
        verify(mockedQuery, times(1)).offset(pageable.getOffset());
        verify(mockedQuery, times(1)).limit(pageable.getPageSize());
        verify(mockedQuery, times(1)).fetch();
        verify(mockedCountQuery, times(1)).from(publisher);
        verify(mockedCountQuery, times(1)).fetchOne();
    }

    @Test
    void testFindAllPublisherList() {
        // Arrange
        List<PublisherGetResponse> mockPublishers = Arrays.asList(
                new PublisherGetResponse(1L, "Publisher 1", true),
                new PublisherGetResponse(2L, "Publisher 2", false)
        );

        // queryFactory.select()가 Projections.constructor와 함께 호출될 때 mockedQuery 반환
        when(queryFactory.select(Projections.constructor(
                PublisherGetResponse.class,
                publisher.publisherId,
                publisher.publisherName,
                publisher.publisherIsUsed)))
                .thenReturn(mockedQuery);

        // mockedQuery.from(publisher) 호출 시 mockedQuery 반환
        when(mockedQuery.from(publisher)).thenReturn(mockedQuery);

        // mockedQuery.fetch() 호출 시 mockPublishers 반환
        when(mockedQuery.fetch()).thenReturn(mockPublishers);

        // Act
        List<PublisherGetResponse> result = publisherCustomRepository.findAllPublisherList();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Publisher 1", result.get(0).getPublisherName());
        assertTrue(result.get(0).isPublisherIsUsed());
        assertEquals("Publisher 2", result.get(1).getPublisherName());
        assertFalse(result.get(1).isPublisherIsUsed());

        // Mock 객체의 메서드 호출 검증
        verify(queryFactory, times(1)).select(any(Expression.class));
        verify(mockedQuery, times(1)).from(publisher);
        verify(mockedQuery, times(1)).fetch();
    }
}