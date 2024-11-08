package com.nhnacademy.ssacthree_shop_api.elastic.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

  @Autowired
  private BookRepository bookRepository;

  // 도서 저장 API
  @PostMapping
  public ResponseEntity<Book> saveBook(@RequestBody Book book) {
    Book savedBook = bookRepository.save(book);
    return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
  }

  // 모든 도서 조회 API
  @GetMapping
  public ResponseEntity<List<Book>> getAllBooks() {
    List<Book> books = bookRepository.findAll();
    return new ResponseEntity<>(books, HttpStatus.OK);
  }

  // 특정 도서 조회 API
  @GetMapping("/{id}")
  public ResponseEntity<Book> getBookById(@PathVariable Long id) {
    Optional<Book> book = bookRepository.findById(id);
    if (book.isPresent()) {
      return new ResponseEntity<>(book.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  // 도서 정보 업데이트 API
  @PutMapping("/{id}")
  public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
    Optional<Book> book = bookRepository.findById(id);
    if (book.isPresent()) {
      Book existingBook = book.get();
      existingBook.setBookName(bookDetails.getBookName());
      existingBook.setBookIndex(bookDetails.getBookIndex());
      existingBook.setBookInfo(bookDetails.getBookInfo());
      existingBook.setBookIsbn(bookDetails.getBookIsbn());
      existingBook.setPublicationDate(bookDetails.getPublicationDate());
      existingBook.setRegularPrice(bookDetails.getRegularPrice());
      existingBook.setSalePrice(bookDetails.getSalePrice());
      existingBook.setPacked(bookDetails.isPacked());
      existingBook.setStock(bookDetails.getStock());
      existingBook.setBookThumbnailImageUrl(bookDetails.getBookThumbnailImageUrl());
      existingBook.setBookViewCount(bookDetails.getBookViewCount());
      existingBook.setBookDiscount(bookDetails.getBookDiscount());
      existingBook.setPublisher(bookDetails.getPublisher());
      Book updatedBook = bookRepository.save(existingBook);
      return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  // 도서 삭제 API
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    if (bookRepository.existsById(id)) {
      bookRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}