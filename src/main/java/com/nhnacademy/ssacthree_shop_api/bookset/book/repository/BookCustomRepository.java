package com.nhnacademy.ssacthree_shop_api.bookset.book.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookBaseResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.dto.BookAuthorDto;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.dto.BookCategoryDto;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.dto.BookTagDto;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCustomRepository {

    // 판매 중, 재고 없는 책을 제목으로 검색
    Page<BookBaseResponse> findBooksByBookName(Pageable pageable, String bookName);

    // 판매 중, 재고 없는 책 전체 검색
    Page<BookBaseResponse> findAllAvailableBooks(Pageable pageable);

    // 재고 없는 책 전체 검색
    Page<BookBaseResponse> findAllBooksByStatusNoStock(Pageable pageable);

    // 판매 중단 책 전체 검색
    Page<BookBaseResponse> findStatusDiscontinued(Pageable pageable);

    // 도서 고유 번호로 도서 찾기
    BookBaseResponse findByBookIsbn(String isbn);

    List<CategoryNameResponse> findCategoriesByBookId(Long bookId);

    List<TagInfoResponse> findTagsByBookId(Long bookId);

    List<AuthorNameResponse> findAuthorsByBookId(Long bookId);

    List<BookCategoryDto> findCategoriesByBookIds(List<Long> bookIds);

    List<BookTagDto> findTagsByBookIds(List<Long> bookIds);

    List<BookAuthorDto> findAuthorsByBookIds(List<Long> bookIds);

    // 조건에 따른 도서 순서(최저 가격 순, 최고 가격 순, 좋아요 순, 판매 수량 순, 조회수 순)

    // 책의 조회수 증가 ..?

    // 카테고리 책 검색(해당 카테고리에 속해있는 책 검색, 하위 카테고리까지 검색해야함)

    // 작가 아이디로 책 검색
    Page<BookBaseResponse> findBooksByAuthorId(Long authorId, Pageable pageable);

    // 작가 이름으로 책 검색

    // 책의 카테고리 루트 검색

    // 태그로 책 검색

    // 책으로 작가 이름 검색
    List<String> findAuthorNamesByBookId(Long bookId);

    // 책으로 출판사 이름 검색
    String findPublisherNameByBookId(Long bookId);

    // 책으로 태그 명 검색
    List<String> findTagNamesByBookId(Long bookId);

    // 책으로 속하는 모든 카테고리 검색 ??
    List<String> findCategoryNamesByBookId(Long bookId);


}
