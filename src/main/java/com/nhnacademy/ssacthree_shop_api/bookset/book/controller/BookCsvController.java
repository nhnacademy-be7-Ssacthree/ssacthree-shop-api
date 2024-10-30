package com.nhnacademy.ssacthree_shop_api.bookset.book.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.author.service.AuthorService;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.CsvParserService;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.service.PublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/upload")
@Slf4j
public class BookCsvController {

    @Autowired
    private CsvParserService csvParserService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private PublisherService publisherService;


    @PostMapping("/books")
    public ResponseEntity<String> saveBooksDB(@RequestParam String filePath){
        try{
            csvParserService.saveBooksFromCsv(filePath);
            return ResponseEntity.ok("CSV uploaded successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CSV upload failed:" + e.getMessage());
        }
    }

    @PostMapping("/authors")
    public ResponseEntity<String> saveAuthorsDB(@RequestParam String filePath){
        try{
            authorService.saveAuthorsFromCsv(filePath);
            return ResponseEntity.ok("CSV uploaded successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CSV upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/publishers")
    public ResponseEntity<String> savePublishersDB(@RequestParam String filePath){
        try{
            publisherService.savePublisherFromCsv(filePath);
            return ResponseEntity.ok("CSV file uploaded successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CSV file upload failed" + e.getMessage());
        }
    }






}