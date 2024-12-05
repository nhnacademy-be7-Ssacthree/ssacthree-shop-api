package com.nhnacademy.ssacthree_shop_api.bookset.book.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.BookNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookCommonService;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.domain.BookLike;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.domain.BookLikeId;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.request.BookLikeRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.response.BookLikeResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.repository.BookLikeRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.commons.exception.NotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookCommonServiceImpl implements BookCommonService {

    private final BookRepository bookRepository;
    private final BookLikeRepository bookLikeRepository;
    private final MemberRepository memberRepository;


    /**
     * 판매 중, 재고 없음 상태 책 모두 조회
     *
     * @param pageable 페이징 처리
     * @return <BookInfoResponse>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookListResponse> getAllAvailableBooks(Pageable pageable) {
        return bookRepository.findAllAvailableBooks(pageable);
    }

    /**
     * 도서 id로 도서 검색합니다.
     *
     * @param bookId 도서 아이디
     * @return 도서 정보
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BookInfoResponse getBook(Long bookId) {
        return bookRepository.findBookById(bookId);
    }

    /**
     * 도서 이름으로 도서를 검색합니다.
     *
     * @param bookName 도서 이름
     * @param pageable 페이징 처리
     * @return Page<BookInfoResponse>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookListResponse> getBooksByBookName(Pageable pageable, String bookName) {
        return bookRepository.findBooksByBookName(pageable, bookName);
    }

    /**
     * 도서 ISBN으로 도서를 검색합니다.
     *
     * @param isbn 도서 ISBN
     * @return 해당 ISBN을 가진 도서
     */
    @Override
    public BookInfoResponse getBooksByBookIsbn(String isbn) {

        return bookRepository.findByBookIsbn(isbn);
    }

    /**
     * 작가 아이디로 작가의 도서를 조회합니다.
     *
     * @param pageable 페이징 처리
     * @param authorId 작가 아이디
     * @return 도서 정보 페이지
     */
    @Override
    public Page<BookListResponse> getBooksByAuthorId(Pageable pageable, Long authorId) {
        return bookRepository.findBooksByAuthorId(authorId, pageable);
    }

    /**
     * 카테고리 아이디로 카테고리의 소속 도서를 조회합니다.
     *
     * @param pageable   페이징 처리
     * @param categoryId 카테고리 아이디
     * @return 도서 정보 페이지
     */
    @Override
    public Page<BookListResponse> getBooksByCategoryId(Pageable pageable, Long categoryId) {
        return bookRepository.findBooksByCategoryId(categoryId, pageable);
    }

    /**
     * 태그 아이디로 소속 도서를 조회합니다.
     *
     * @param pageable 페이징 처리
     * @param tagId    태그 아이디
     * @return 도서 정보 페이지
     */
    @Override
    public Page<BookListResponse> getBooksByTagId(Pageable pageable, Long tagId) {
        return bookRepository.findBooksByTagId(tagId, pageable);
    }

    /**
     * 회원의 좋아요 도서 목록을 검색합니다
     *
     * @param pageable   페이징 처리
     * @param customerId 멤버 아이디
     * @return 도서 정보 페이지
     */
    @Override
    public Page<BookListResponse> getBooksByMemberId(Pageable pageable, Long customerId) {
        return bookRepository.findBookLikesByCustomerId(customerId, pageable);
    }

    /**
     * 회원의 좋아요 도서 아이디 리스트를 가져옵니다
     *
     * @param customerId 회원 아이디
     * @return 좋아요한 도서 아이디 리스트
     */
    @Override
    public List<Long> getLikedBooksIdForCurrentUser(Long customerId) {
        List<Long> bookIdList = bookRepository.findLikedBookIdByCustomerId(customerId);
        return bookIdList != null ? bookIdList : List.of();
    }

    /**
     * 도서 아이디로 카테고리 리스트 가져오기
     *
     * @param bookId 도서 아이디
     * @return 카테고리 리스트
     */
    @Override
    public List<CategoryNameResponse> getCategoriesByBookId(Long bookId) {
        if (bookRepository.findById(bookId).isEmpty()) {
            throw new NotFoundException("해당 도서가 존재하지 않습니다.");
        }

        return bookRepository.findCategoriesByBookId(bookId);
    }

    /**
     * 좋아요 정보를 저장합니다.
     *
     * @param bookLikeRequest 좋아요 요청
     * @param customerId      좋아요한 멤버의 customerId
     * @return 좋아요 정보
     */
    @Override
    public BookLikeResponse saveBookLike(BookLikeRequest bookLikeRequest, Long customerId) {
        Book book = bookRepository.findById(bookLikeRequest.getBookId())
            .orElseThrow(() -> new BookNotFoundException("해당 도서를 찾을 수 없습니다."));
        Member member = memberRepository.findById(customerId)
            .orElseThrow(() -> new NotFoundException("해당 회원이 존재하지않습니다."));

        BookLike bookLike = new BookLike(book, member);

        bookLikeRepository.save(bookLike);

        return new BookLikeResponse(bookLikeRequest,
            bookRepository.findBookLikeByBookId(book.getBookId()));
    }

    /**
     * 좋아요 정보를 삭제합니다.
     *
     * @param bookId     좋아요를 취소할 책 아이디
     * @param customerId 좋아요를 취소하는 멤버의 customerId
     * @return 좋아요 삭제 성공 여부
     */
    @Override
    public Boolean deleteBookLike(Long bookId, Long customerId) {

        BookLikeId bookLikeId = new BookLikeId(bookId, customerId);

        if (bookLikeRepository.existsById(bookLikeId)) {
            bookLikeRepository.deleteById(bookLikeId);
            return true;
        } else {
            throw new NotFoundException(
                "해당 좋아요 기록이 존재하지 않습니다.");
        }

    }

}
