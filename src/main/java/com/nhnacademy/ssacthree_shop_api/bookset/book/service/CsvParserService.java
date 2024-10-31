package com.nhnacademy.ssacthree_shop_api.bookset.book.service;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.repository.AuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.CsvProcessingException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.repository.BookAuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherRepository;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CsvParserService {
    private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Autowired
    BookRepository bookRepository;
    @Autowired
    PublisherRepository publisherRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    private BookAuthorRepository bookAuthorRepository;


    public void saveBooksFromCsv(String filePath){

        try{

            CSVParserBuilder parserBuilder = new CSVParserBuilder().withQuoteChar('\''); // 작은따옴표로 감싸진 데이터 처리
            CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(parserBuilder.build())
                .build();

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                Book book = new Book();
                book.setBookId(Long.parseLong(nextLine[0].replace("'", "").trim()));
                book.setBookName(nextLine[1]);
                book.setBookIndex("목차 정보가 없습니다.");
                book.setBookInfo(nextLine[7]);
                book.setBookIsbn(nextLine[8]);


                String publicationDateString = nextLine[4].replace("'", "").trim();
                try {
                    LocalDate publicationDate = LocalDate.parse(publicationDateString, dateFormatter);
                    book.setPublicationDate(publicationDate.atStartOfDay());
                } catch (DateTimeParseException e) {
                    log.error("boot id의 잘못된 날짜 형식 {}: {}", book.getBookId(), publicationDateString);
                    continue;
                }

                book.setRegularPrice(Integer.parseInt(nextLine[5].replace("'","").trim()));
                book.setSalePrice(Integer.parseInt(nextLine[6].replace("'","").trim()));
                book.setPacked(true);
                book.setStock(10);
                book.setBookThumbnailImageUrl(nextLine[10]);


                String authorName = distinguishAuthors(nextLine[2].trim());
                Author author = authorRepository.findByAuthorName(authorName)
                        .orElseGet(() -> {
                            Author newAuthor = new Author();
                            newAuthor.setAuthorName(authorName);
                            newAuthor.setAuthorInfo("");
                            return authorRepository.save(newAuthor);
                        });

                String publisherName = nextLine[3].trim();
                Optional<Publisher> optionalPublisher = publisherRepository.findByPublisherName(publisherName);
                if (optionalPublisher.isPresent()) {
                    book.setPublisher(optionalPublisher.get());
                } else {
                    log.warn("Publisher not found for name: {}", publisherName);
                    continue;
                }

                Book bookResult = bookRepository.save(book);
                bookAuthorRepository.save(new BookAuthor(bookResult, author));

            }
        }catch (IOException | CsvValidationException e) {
            throw new CsvProcessingException("Failed to process CSV file" + e.getMessage());
        }
    }

    private String distinguishAuthors(String input){
        String[] authorsParts = input.split(",");
        return authorsParts[0].replaceAll("\\(.*?\\)","").trim();
    }

}
