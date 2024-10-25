package com.nhnacademy.ssacthree_shop_api.bookset.book.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.book.service.CsvParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookCsvController {

    @Autowired
    private CsvParserService csvParserService;

    @GetMapping("/upload-csv")
    public String uploadCsv(@RequestParam String filePath){
        csvParserService.saveBooksFromCsv(filePath);
        return "success";
    }

}