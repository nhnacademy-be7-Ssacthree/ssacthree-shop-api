package com.nhnacademy.ssacthree_shop_api.bookset.author.service.impl;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.author.exception.AuthorNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.author.repository.AuthorRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService; // AuthorService의 구현체

    private static final String AUTHOR_NAME = "Author name";
    private static final String AUTHOR_INFO = "Author Info";
    private static final String UPDATED_AUTHOR_NAME = "Updated Author name";
    private static final String UPDATED_AUTHOR_INFO = "Updated Author Info";
    private static final String AUTHOR_CREATE_ERROR_MESSAGE = "작가 정보 생성에 실패했습니다.";
    private static final String AUTHOR_ID_ERROR_MESSAGE = "authorId는 1보다 작을 수 없습니다.";
    private static final String AUTHOR_NOT_FOUND_MESSAGE = "해당 아이디를 찾을 수 없습니다.:";
    private static final Long VALID_AUTHOR_ID = 1L;
    private static final Long INVALID_AUTHOR_ID = 0L;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAuthor_shouldSaveAndReturnAuthor_whenValidRequestProvided() {
        // given
        AuthorCreateRequest validRequest = new AuthorCreateRequest(AUTHOR_NAME, AUTHOR_INFO);
        Author expectedAuthor = new Author(AUTHOR_NAME, AUTHOR_INFO);

        when(authorRepository.save(any(Author.class))).thenReturn(expectedAuthor);

        // when
        Author result = authorService.createAuthor(validRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAuthorName()).isEqualTo(AUTHOR_NAME);
        assertThat(result.getAuthorInfo()).isEqualTo(AUTHOR_INFO);

        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void createAuthor_shouldThrowException_whenRequestIsNull() {
        // given
        AuthorCreateRequest nullRequest = null;

        // when & then
        assertThatThrownBy(() -> authorService.createAuthor(nullRequest))
            .isInstanceOf(AuthorNotFoundException.class)
            .hasMessage(AUTHOR_CREATE_ERROR_MESSAGE);
    }

    @Test
    void createAuthor_shouldThrowException_whenAuthorNameIsNull() {
        // given
        AuthorCreateRequest requestWithNullName = new AuthorCreateRequest(null, AUTHOR_INFO);

        // when & then
        assertThatThrownBy(() -> authorService.createAuthor(requestWithNullName))
            .isInstanceOf(AuthorNotFoundException.class)
            .hasMessage(AUTHOR_CREATE_ERROR_MESSAGE);
    }

    @Test
    void createAuthor_shouldThrowException_whenAuthorInfoIsNull() {
        // given
        AuthorCreateRequest requestWithNullInfo = new AuthorCreateRequest(AUTHOR_NAME, null);

        // when & then
        assertThatThrownBy(() -> authorService.createAuthor(requestWithNullInfo))
            .isInstanceOf(AuthorNotFoundException.class)
            .hasMessage(AUTHOR_CREATE_ERROR_MESSAGE);
    }

    @Test
    void updateAuthor_shouldUpdateAndReturnAuthor_whenValidRequestProvided() {
        // given
        AuthorUpdateRequest validRequest = new AuthorUpdateRequest(VALID_AUTHOR_ID, UPDATED_AUTHOR_NAME, UPDATED_AUTHOR_INFO);
        Author existingAuthor = new Author(AUTHOR_NAME, AUTHOR_INFO);
        existingAuthor.setAuthorId(VALID_AUTHOR_ID);

        when(authorRepository.findById(VALID_AUTHOR_ID)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(existingAuthor);

        // when
        Author result = authorService.updateAuthor(validRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAuthorName()).isEqualTo(UPDATED_AUTHOR_NAME);
        assertThat(result.getAuthorInfo()).isEqualTo(UPDATED_AUTHOR_INFO);

        verify(authorRepository, times(1)).findById(VALID_AUTHOR_ID);
        verify(authorRepository, times(1)).save(existingAuthor);
    }

    @Test
    void updateAuthor_shouldThrowException_whenAuthorIdIsInvalid() {
        // given
        AuthorUpdateRequest invalidRequest = new AuthorUpdateRequest(INVALID_AUTHOR_ID, AUTHOR_NAME, AUTHOR_INFO);

        // when & then
        assertThatThrownBy(() -> authorService.updateAuthor(invalidRequest))
            .isInstanceOf(AuthorNotFoundException.class)
            .hasMessage(AUTHOR_ID_ERROR_MESSAGE);

        verify(authorRepository, never()).findById(anyLong());
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void updateAuthor_shouldThrowException_whenAuthorNotFound() {
        // given
        AuthorUpdateRequest requestWithNonExistentId = new AuthorUpdateRequest(VALID_AUTHOR_ID, AUTHOR_NAME, AUTHOR_INFO);

        when(authorRepository.findById(VALID_AUTHOR_ID)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authorService.updateAuthor(requestWithNonExistentId))
            .isInstanceOf(AuthorNotFoundException.class)
            .hasMessage(AUTHOR_NOT_FOUND_MESSAGE + VALID_AUTHOR_ID);

        verify(authorRepository, times(1)).findById(VALID_AUTHOR_ID);
        verify(authorRepository, never()).save(any(Author.class));
    }
    @Test
    void getAllAuthors_shouldReturnPagedAuthors() {
        // given
        Pageable pageable = PageRequest.of(0, 2); // 첫 번째 페이지, 크기 2
        List<AuthorGetResponse> authorList = List.of(
            new AuthorGetResponse(1L, "Author1", "Info1"),
            new AuthorGetResponse(2L, "Author2", "Info2")
        );
        Page<AuthorGetResponse> pagedAuthors = new PageImpl<>(authorList, pageable, authorList.size());

        when(authorRepository.findAllAuthors(pageable)).thenReturn(pagedAuthors);

        // when
        Page<AuthorGetResponse> result = authorService.getAllAuthors(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2); // 총 원소 수
        assertThat(result.getContent()).hasSize(2);         // 페이지 크기
        assertThat(result.getContent().get(0).getAuthorName()).isEqualTo("Author1");
    }

    @Test
    void getAllAuthorList_shouldReturnAllAuthors() {
        // given
        List<AuthorGetResponse> authors = List.of(
            new AuthorGetResponse(1L, "Author1", "Info1"),
            new AuthorGetResponse(2L, "Author2", "Info2")
        );

        when(authorRepository.findAllAuthorList()).thenReturn(authors);

        // when
        List<AuthorGetResponse> result = authorService.getAllAuthorList();

        // then
        assertThat(result).hasSize(2); // List 크기 검증
        assertThat(result.get(0).getAuthorName()).isEqualTo("Author1"); // 첫 번째 요소 검증
    }

    @Test
    void getAuthorById_shouldReturnAuthorWhenExists() {
        // given
        Long authorId = 1L;
        AuthorGetResponse author = new AuthorGetResponse(authorId, AUTHOR_NAME, AUTHOR_INFO);

        when(authorRepository.findAuthorById(authorId)).thenReturn(author);

        // when
        AuthorGetResponse result = authorService.getAuthorById(authorId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAuthorName()).isEqualTo(AUTHOR_NAME);
        verify(authorRepository, times(1)).findAuthorById(authorId);
    }

    @Test
    void getAuthorById_shouldThrowExceptionWhenNotFound() {
        // given
        Long authorId = 1L;
        when(authorRepository.findAuthorById(authorId)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> authorService.getAuthorById(authorId))
            .isInstanceOf(AuthorNotFoundException.class)
            .hasMessageContaining(AUTHOR_NOT_FOUND_MESSAGE + authorId);

        verify(authorRepository, times(1)).findAuthorById(authorId);
    }

    @Test
    void deleteAuthor_shouldDeleteAuthorWhenExists() {
        // given
        Long authorId = 1L;

        when(authorRepository.existsById(authorId)).thenReturn(true);

        // when
        authorService.deleteAuthor(authorId);

        // then
        verify(authorRepository, times(1)).existsById(authorId);
        verify(authorRepository, times(1)).deleteById(authorId);
    }
    @Test
    void deleteAuthor_shouldThrowExceptionWhenNotFound() {
        // given
        Long authorId = 1L;

        when(authorRepository.existsById(authorId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> authorService.deleteAuthor(authorId))
            .isInstanceOf(AuthorNotFoundException.class)
            .hasMessageContaining(AUTHOR_NOT_FOUND_MESSAGE + authorId);

        verify(authorRepository, times(1)).existsById(authorId);
        verify(authorRepository, never()).deleteById(authorId);
    }



}