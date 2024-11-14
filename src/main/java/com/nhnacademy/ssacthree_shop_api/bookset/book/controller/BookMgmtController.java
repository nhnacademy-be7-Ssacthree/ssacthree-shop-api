package com.nhnacademy.ssacthree_shop_api.bookset.book.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookBaseResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookSearchResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookMgmtService;
import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/admin/books")
public class BookMgmtController {
    public static final String BOOK_CREATE_SUCCESS_MESSAGE = "도서 정보 생성 성공";
    public static final String BOOK_UPDATE_SUCCESS_MESSAGE = "도서 정보 수정 성공";
    public static final String BOOK_DELETE_SUCCESS_MESSAGE = "도서 정소 삭제 성공";

    private final BookMgmtService bookMgmtService;

    @GetMapping
    public ResponseEntity<Page<BookSearchResponse>> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(defaultValue = "bookName") String[] sortBy
                                                              ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<BookSearchResponse> books = bookMgmtService.getAllBooks(pageable);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createBook(@Valid @RequestBody BookSaveRequest bookSaveRequest) {
//        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON)
//                .body(bookMgmtService.saveBook(bookSaveRequest));
        bookMgmtService.saveBook(bookSaveRequest);
        MessageResponse messageResponse = new MessageResponse(BOOK_CREATE_SUCCESS_MESSAGE);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @PutMapping("/{book-id}")
    public ResponseEntity<MessageResponse> updateBook(@PathVariable(name = "book-id") Long bookId, @Valid @RequestBody BookSaveRequest bookSaveRequest) {
        bookMgmtService.updateBook(bookId, bookSaveRequest);
        MessageResponse messageResponse = new MessageResponse(BOOK_UPDATE_SUCCESS_MESSAGE);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @DeleteMapping("/{book-id}")
    public ResponseEntity<MessageResponse> deleteBook(@PathVariable(name = "book-id") Long bookId, BookSaveRequest bookSaveRequest) {
        bookMgmtService.deleteBook(bookId, bookSaveRequest);
        MessageResponse messageResponse = new MessageResponse(BOOK_DELETE_SUCCESS_MESSAGE);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @GetMapping("/{book-id}")
    public ResponseEntity<Page<BookInfoResponse>> getBookByBookId(@PathVariable(name = "book-id") Long bookId,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(defaultValue = "bookName") String[] sortBy){


        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<BookInfoResponse> books = bookMgmtService.getBookById(bookId, pageable);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }



}
