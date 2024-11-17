package com.nhnacademy.ssacthree_shop_api.bookset.book.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookCommonService;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.commons.paging.PageRequestBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/books")
public class BookCommonController {

    private final BookCommonService bookCommonService;

    /**
     * 책을 불러옵니다. (판매 중, 재고 없음 경우만 표시)
     * @param page 현재 요청하려는 페이지 번호
     * @param size 한 페이지에 표시할 데이터의 개수
     * @return ResponseEntity<Page<BookInfoResponse>>
     */
    @GetMapping
    public ResponseEntity<Page<BookInfoResponse>> getAllAvailableBooks(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size,
                                                                 @RequestParam(defaultValue = "bookName:asc") String[] sort) {
        Pageable pageable = PageRequestBuilder.createPageable(page, size, sort);
        Page<BookInfoResponse> books = bookCommonService.getAllAvailableBooks(pageable);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * 도서 아이디로 도서를 조회합니다.
     * @param bookId 도서 아이디
     * @return 도서 정보
     */
    @GetMapping("/{book-id}")
    public ResponseEntity<BookInfoResponse> getBookById(@PathVariable(name="book-id") Long bookId) {
        BookInfoResponse book = bookCommonService.getBook(bookId);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    /**
     * 도서 이름을 검색하여 검색한 이름을 포함하는 도서들을 조회합니다.
     * @param page 현재 요청하려는 페이지 번호
     * @param size 한 페이지에 표시할 데이터의 개수
     * @param sort 정렬 조건 (여러개의 정렬 조건을 설정 가능하도록 배열 형태로)
     * @param title 도서 제목
     * @return 검색한 이름을 포함한 도서에 대한 페이지
     */
    @GetMapping("/search")
    public ResponseEntity<Page<BookInfoResponse>> getBooksByTitle(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam(defaultValue = "bookName:asc") String[] sort,
                                                                  @RequestParam String title) {
        Pageable pageable = PageRequestBuilder.createPageable(page, size, sort);
        Page<BookInfoResponse> books = bookCommonService.getBooksByBookName(pageable, title);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * 작가 아이디로 작가의 도서를 조회합니다.
     * @param page 현재 요청하려는 페이지 번호
     * @param size 한 페이지에 표시할 데이터의 개수
     * @param sort 정렬 조건 (여러개의 정렬 조건을 설정 가능하도록 배열 형태로)
     * @param authorId 작가 아이디
     * @return 작가의 도서 페이지
     */
    //todo:@RequestParam 써서 해봐야겠다.
    @GetMapping("/author/{author-id}")
    public ResponseEntity<Page<BookInfoResponse>> getBookByAuthorId(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(defaultValue = "bookName:asc") String[] sort,
                                                                    @PathVariable(name = "author-id") Long authorId) {
        Pageable pageable = PageRequestBuilder.createPageable(page, size, sort);
        Page<BookInfoResponse> books = bookCommonService.getBooksByAuthorId(pageable, authorId);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{book-id}/categories")
    public ResponseEntity<List<CategoryNameResponse>> getCategoriesByBookId(@PathVariable(name = "book-id") Long bookId) {
        List<CategoryNameResponse> categories = bookCommonService.getCategoriesByBookId(bookId);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
