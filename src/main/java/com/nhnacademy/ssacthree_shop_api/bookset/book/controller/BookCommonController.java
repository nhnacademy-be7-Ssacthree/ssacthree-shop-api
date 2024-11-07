package com.nhnacademy.ssacthree_shop_api.bookset.book.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/books")
public class BookCommonController {

    private final BookCommonService bookCommonService;

    /**
     * 최근에 출판한 순서대로 책을 불러옵니다. (판매 중, 재고 없음 경우만 표시)
     * @param pageable 페이징 처리
     * @return 최근에 출판한 책 순서대로 페이징
     */
    //todo: 도서 조회 기본이 최근 출판 정보로 하는게 나으려나..? 일단 그렇게 했는데.. 추후 변경할 수도.
    @GetMapping
    public ResponseEntity<Page<BookInfoResponse>> getRecentBooks(Pageable pageable) {
        Page<BookInfoResponse> books = bookCommonService.getRecentBooks(pageable);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * 도서 아이디 순서대로 책을 불러옵니다. (판매 중, 재고 없을 경우만 표시)
     * @param pageable 페이징 처리
     * @return 도서 아이디 순서대로 도서를 페이징
     */
    @GetMapping("/available-books")
    public ResponseEntity<Page<BookInfoResponse>> getAvailableBooks(Pageable pageable) {
        Page<BookInfoResponse> books = bookCommonService.getAllAvailableBooks(pageable);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * 도서 아이디로 도서를 조회합니다.
     * @param bookId 도서 아이디
     * @return 도서 정보
     */
    @GetMapping("/{bookId}")
    public ResponseEntity<BookInfoResponse> getBookById(@PathVariable Long bookId) {
        BookInfoResponse book = bookCommonService.getBook(bookId);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    /**
     * 도서 이름을 검색하여 검색한 이름을 포함하는 도서들을 조회합니다.
     * @param pageable 페이징 처리
     * @param title 도서 제목
     * @return 검색한 이름을 포함한 도서에 대한 페이지
     */
    @GetMapping("/search")
    public ResponseEntity<Page<BookInfoResponse>> getBooksByTitle(Pageable pageable, @RequestParam String title) {
        Page<BookInfoResponse> books = bookCommonService.getBooksByBookName(pageable, title);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
