//package com.nhnacademy.ssacthree_shop_api.bookset.book.controller;
//
//import com.nhnacademy.ssacthree_shop_api.bookset.book.service.CsvParserService;
//import com.nhnacademy.ssacthree_shop_api.bookset.publisher.service.PublisherService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/shop/upload")
//@RequiredArgsConstructor
//@Slf4j
//public class BookCsvController {
//
//
//    private final CsvParserService csvParserService;
//
//    private final PublisherService publisherService;
//
//
//    @PostMapping("/bookauthors")
//    public ResponseEntity<String> saveBooksDB(@RequestParam String filePath){
//        try{
//            csvParserService.saveBooksFromCsv(filePath);
//            return ResponseEntity.ok("도서와 작가가 업로드 되었습니다.");
//        }catch(Exception e){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("도서와 작가 업로드에 실패했습니다.:" + e.getMessage());
//        }
//    }
//
//    @PostMapping("/publishers")
//    public ResponseEntity<String> savePublishersDB(@RequestParam String filePath){
//        try{
//            publisherService.savePublisherFromCsv(filePath);
//            return ResponseEntity.ok("출판사가 업로드 되었습니다.");
//        }catch(Exception e){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("출판사 업로드에 실패" + e.getMessage());
//        }
//    }
//}