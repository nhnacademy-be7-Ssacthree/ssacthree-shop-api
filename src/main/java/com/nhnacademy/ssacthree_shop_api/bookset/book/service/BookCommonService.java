package com.nhnacademy.ssacthree_shop_api.bookset.book.service;

import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.request.BookLikeRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.response.BookLikeResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookCommonService {
    // 도서 전체 조회(판매 중, 재고 없음)
    Page<BookListResponse> getAllAvailableBooks(Pageable pageable);

    // 도서 아이디 검색
    BookInfoResponse getBook(Long bookId);

    // 도서 이름 검색
    Page<BookListResponse> getBooksByBookName(Pageable pageable, String bookName);

    // 도서 ISBN으로 도서 검색
    BookInfoResponse getBooksByBookIsbn(String isbn);

    // 작가 아이디로 도서 찾기
    Page<BookListResponse> getBooksByAuthorId(Pageable pageable, Long authorId);

    // 도서의 카테고리 찾기
    List<CategoryNameResponse> getCategoriesByBookId(Long bookId);

    // 카테고리 아이디로 소속 도서 찾기
    Page<BookListResponse> getBooksByCategoryId(Pageable pageable, Long categoryId);

    // 태그 아이디로 소속 도서 찾기
    Page<BookListResponse> getBooksByTagId(Pageable pageable, Long tagId);

    // 회원의 좋아요 도서 목록 검색
    Page<BookListResponse> getBooksByMemberId(Pageable pageable, Long customerId);

    // 회원의 좋아요 도서 아이디 리스트
    List<Long> getLikedBooksIdForCurrentUser(Long customerId);

    // 좋아요 생성
    BookLikeResponse saveBookLike(BookLikeRequest bookLikeRequest, Long customerId);

    // 좋아요 삭제
    BookLikeResponse deleteBookLike(Long bookId, Long customerId);

}
