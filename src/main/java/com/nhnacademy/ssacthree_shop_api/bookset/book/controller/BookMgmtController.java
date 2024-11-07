package com.nhnacademy.ssacthree_shop_api.bookset.book.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookMgmtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/admin/books")
public class BookMgmtController {

    private final BookMgmtService bookMgmtService;

    @PostMapping
    public ResponseEntity<BookInfoResponse> createBook(@RequestBody BookSaveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON)
                .body(bookMgmtService.saveBook(request));
    }

    @PutMapping
    public ResponseEntity<BookInfoResponse> updateBook(@RequestBody BookSaveRequest request) {
        return null;
    }


}
