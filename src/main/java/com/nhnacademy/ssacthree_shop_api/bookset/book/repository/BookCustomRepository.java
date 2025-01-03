package com.nhnacademy.ssacthree_shop_api.bookset.book.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookCustomRepository {

    // 판매 중, 재고 없는 책을 제목으로 검색
    Page<BookListResponse> findBooksByBookName(Pageable pageable, String bookName);

    // 판매 중, 재고 없는 책 전체 검색
    Page<BookListResponse> findAllAvailableBooks(Pageable pageable);

    // 재고 없는 책 전체 검색
    Page<BookListResponse> findAllBooksByStatusNoStock(Pageable pageable);

    // 판매 중단 책 전체 검색
    Page<BookListResponse> findStatusDiscontinued(Pageable pageable);

    // 도서 고유 번호로 도서 찾기
    BookInfoResponse findByBookIsbn(String isbn);

    List<CategoryNameResponse> findCategoriesByBookId(Long bookId);

    List<TagInfoResponse> findTagsByBookId(Long bookId);

    List<AuthorNameResponse> findAuthorsByBookId(Long bookId);

    // 카테고리 책 검색(해당 카테고리에 속해있는 책 검색, 하위 카테고리까지 검색해야함)
    Page<BookListResponse> findBooksByCategoryId(Long categoryId, Pageable pageable);

    // 작가 아이디로 책 검색
    Page<BookListResponse> findBooksByAuthorId(Long authorId, Pageable pageable);

    // 태그로 책 검색
    Page<BookListResponse> findBooksByTagId(Long tagId, Pageable pageable);

    // 책으로 작가 이름 검색
    List<String> findAuthorNamesByBookId(Long bookId);

    // 책으로 출판사 이름 검색
    String findPublisherNameByBookId(Long bookId);

    // 책으로 태그 명 검색
    List<String> findTagNamesByBookId(Long bookId);

    // 책으로 속하는 모든 카테고리 검색 ??
    List<String> findCategoryNamesByBookId(Long bookId);

    // 도서 아이디로 도서 검색
    BookInfoResponse findBookById(Long bookId);

    // 회원의 좋아요 도서 목록 검색
    Page<BookListResponse> findBookLikesByCustomerId(Long customerId, Pageable pageable);

    // 회원의 좋아요 도서 아이디 리스트
    List<Long> findLikedBookIdByCustomerId(Long customerId);

    // 도서의 좋아요 수
    Long findBookLikeByBookId(Long bookId);

    // 도서의 리뷰 수
    Long findReviewCountByBookId(Long bookId);

    // 도서의 평점 평균
    Double findReviewRateAverageByBookId(Long bookId);
}
