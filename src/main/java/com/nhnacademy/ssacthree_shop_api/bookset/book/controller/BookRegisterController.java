package com.nhnacademy.ssacthree_shop_api.bookset.book.controller;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.BookDto;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/books")
public class BookRegisterController {
    private final BookService bookService;
    @Autowired
    public BookRegisterController(BookService bookService) {
        this.bookService = bookService;
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerBook(@RequestBody BookDto bookDto) {
        bookService.registerBook(bookDto);
        return new ResponseEntity<>("Book registered successfully", HttpStatus.CREATED);
    }
}


