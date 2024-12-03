package com.nhnacademy.ssacthree_shop_api.bookset.book.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookBaseResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookListBaseResponse;
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
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookCommonServiceImpl implements BookCommonService {
    private final BookRepository bookRepository;
    private final BookLikeRepository bookLikeRepository;
    private final MemberRepository memberRepository;

    /**
     * BookInfoResponse에 출판사, 카테고리, 태그, 작가 정보를 주입해주기 위한 메소드
     * @param bookBaseResponse 도서 기본 정보(아직 출판사, 카테고리, 태그, 작가 정보 비어 있음)
     */
    private BookInfoResponse addAssociatedDataToBookInfoResponse(BookBaseResponse bookBaseResponse){
        if(bookBaseResponse == null){
            throw new NotFoundException("해당 도서를 찾을 수 없습니다");
        }
        BookInfoResponse bookInfoResponse = new BookInfoResponse(bookBaseResponse);

        // 카테고리 설정
        List<CategoryNameResponse> categories = bookRepository.findCategoriesByBookId(bookInfoResponse.getBookId());
        bookInfoResponse.setCategories(categories);

        // 태그 설정
        List<TagInfoResponse> tags = bookRepository.findTagsByBookId(bookInfoResponse.getBookId());
        bookInfoResponse.setTags(tags);

        // 작가 설정
        List<AuthorNameResponse> authors = bookRepository.findAuthorsByBookId(bookInfoResponse.getBookId());
        bookInfoResponse.setAuthors(authors);

        return bookInfoResponse;
    }

    private Page<BookListResponse> addAssociatedDataToBookListResponsePage(Page<BookListBaseResponse> booksPage) {
        if (booksPage == null) {
            throw new NotFoundException("해당 도서를 찾을 수 없습니다");
        }

        List<BookListResponse> bookListResponse = booksPage.getContent().stream()
                .map(baseResponse -> {
                    BookListResponse response = new BookListResponse(baseResponse);

                    response.setCategories(bookRepository.findCategoriesByBookId(baseResponse.getBookId()));
                    response.setTags(bookRepository.findTagsByBookId(baseResponse.getBookId()));
                    response.setAuthors(bookRepository.findAuthorsByBookId(baseResponse.getBookId()));
                    response.setLikeCount(bookRepository.findBookLikeByBookId(baseResponse.getBookId()));
                    response.setReviewCount(bookRepository.findReviewCountByBookId(baseResponse.getBookId()));
                    response.setReviewRateAverage(bookRepository.findReviewRateAverageByBookId(baseResponse.getBookId()));

                    return response;
                })
                .toList();

        return new PageImpl<>(bookListResponse, booksPage.getPageable(), booksPage.getTotalElements());
    }

    /**
     * 판매 중, 재고 없음 상태 책 모두 조회
     * @param pageable 페이징 처리
     * @return <BookInfoResponse>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookListResponse> getAllAvailableBooks(Pageable pageable) {
        return addAssociatedDataToBookListResponsePage(bookRepository.findAllAvailableBooks(pageable));
    }

    /**
     * 도서 id로 도서 검색합니다.
     * @param bookId 도서 아이디
     * @return 도서 정보
     */
    @Override
    public BookInfoResponse getBook(Long bookId) {
        return addAssociatedDataToBookInfoResponse(bookRepository.findBookById(bookId));
    }

    /**
     * 도서 이름으로 도서를 검색합니다.
     * @param bookName 도서 이름
     * @param pageable 페이징 처리
     * @return Page<BookInfoResponse>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookListResponse> getBooksByBookName(Pageable pageable, String bookName) {
        return addAssociatedDataToBookListResponsePage(bookRepository.findBooksByBookName(pageable, bookName));
    }

    /**
     * 도서 ISBN으로 도서를 검색합니다.
     * @param isbn 도서 ISBN
     * @return 해당 ISBN을 가진 도서
     */
    @Override
    public BookInfoResponse getBooksByBookIsbn(String isbn) {

        return addAssociatedDataToBookInfoResponse(bookRepository.findByBookIsbn(isbn));
    }

    /**
     * 작가 아이디로 작가의 도서를 조회합니다.
     * @param pageable 페이징 처리
     * @param authorId 작가 아이디
     * @return 도서 정보 페이지
     */
    @Override
    public Page<BookListResponse> getBooksByAuthorId(Pageable pageable, Long authorId) {
        return addAssociatedDataToBookListResponsePage(bookRepository.findBooksByAuthorId(authorId, pageable));
    }

    /**
     * 카테고리 아이디로 카테고리의 소속 도서를 조회합니다.
     * @param pageable 페이징 처리
     * @param categoryId 카테고리 아이디
     * @return 도서 정보 페이지
     */
    @Override
    public Page<BookListResponse> getBooksByCategoryId(Pageable pageable, Long categoryId) {
        return addAssociatedDataToBookListResponsePage(bookRepository.findBooksByCategoryId(categoryId, pageable));
    }

    /**
     * 태그 아이디로 소속 도서를 조회합니다.
     * @param pageable 페이징 처리
     * @param tagId 태그 아이디
     * @return 도서 정보 페이지
     */
    @Override
    public Page<BookListResponse> getBooksByTagId(Pageable pageable, Long tagId) {
        return addAssociatedDataToBookListResponsePage(bookRepository.findBooksByTagId(tagId, pageable));
    }

    /**
     * 회원의 좋아요 도서 목록을 검색합니다
     * @param pageable 페이징 처리
     * @param customerId 멤버 아이디
     * @return 도서 정보 페이지
     */
    @Override
    public Page<BookListResponse> getBooksByMemberId(Pageable pageable, Long customerId) {
        return addAssociatedDataToBookListResponsePage(bookRepository.findBookLikesByCustomerId(customerId, pageable));
    }

    /**
     * 회원의 좋아요 도서 아이디 리스트를 가져옵니다
     * @param customerId 회원 아이디
     * @return 좋아요한 도서 아이디 리스트
     */
    @Override
    public List<Long> getLikedBooksIdForCurrentUser(Long customerId) {
        List<Long> bookIdList = bookRepository.findLikedBookIdByCustomerId(customerId);
        return bookIdList!= null ? bookIdList : List.of();
    }

    /**
     * 도서 아이디로 카테고리 리스트 가져오기
     * @param bookId 도서 아이디
     * @return 카테고리 리스트
     */
    @Override
    public List<CategoryNameResponse> getCategoriesByBookId(Long bookId) {
        if(bookRepository.findById(bookId).isEmpty()) {
            throw new NotFoundException("해당 도서가 존재하지 않습니다.");
        }

        return bookRepository.findCategoriesByBookId(bookId);
    }

    @Override
    public BookLikeResponse saveBookLike(BookLikeRequest bookLikeRequest, Long customerId) {
        Book book = bookRepository.findById(bookLikeRequest.getBookId()).orElseThrow(() -> new BookNotFoundException("해당 도서를 찾을 수 없습니다."));
        Member member = memberRepository.findById(customerId).orElseThrow(() -> new NotFoundException("해당 회원이 존재하지않습니다."));

        BookLike bookLike = new BookLike(book, member);

        bookLikeRepository.save(bookLike);

        return new BookLikeResponse(bookLikeRequest, bookRepository.findBookLikeByBookId(book.getBookId()));
    }

    @Override
    public Boolean deleteBookLike(Long bookId, Long customerId) {

        BookLikeId bookLikeId = new BookLikeId(bookId, customerId);

        if (bookLikeRepository.existsById(bookLikeId)) {
            bookLikeRepository.deleteById(bookLikeId);
            return true;
        } else {
            throw new NotFoundException("해당 좋아요 기록이 존재하지 않습니다.");
        }

    }

}
