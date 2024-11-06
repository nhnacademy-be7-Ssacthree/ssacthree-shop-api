package com.nhnacademy.ssacthree_shop_api.bookset.book.service;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorDto;
import com.nhnacademy.ssacthree_shop_api.bookset.author.repository.AuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.BookDto;
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
                BookDto bookDto = new BookDto();
                bookDto.setBookId(Long.parseLong(nextLine[0].replace("'", "").trim()));
                bookDto.setBookName(nextLine[1]);
                bookDto.setBookIndex("목차 정보가 없습니다.");
                bookDto.setBookInfo(nextLine[7]);
                bookDto.setBookIsbn(nextLine[8]);


                String publicationDateString = nextLine[4].replace("'", "").trim();
                try {
                    LocalDate publicationDate = LocalDate.parse(publicationDateString, dateFormatter);
                    bookDto.setPublicationDate(publicationDate.atStartOfDay());
                } catch (DateTimeParseException e) {
                    log.error("boot id의 잘못된 날짜 형식 {}: {}", bookDto.getBookId(), publicationDateString);
                    continue;
                }

                bookDto.setRegularPrice(Integer.parseInt(nextLine[5].replace("'","").trim()));
                bookDto.setSalePrice(Integer.parseInt(nextLine[6].replace("'","").trim()));
                bookDto.setPacked(true);
                bookDto.setStock(10);
                bookDto.setBookThumbnailImageUrl(nextLine[10]);


                // Author 처리
                String authorName = distinguishAuthors(nextLine[2].trim());
                AuthorDto authorDto = new AuthorDto();
                authorDto.setAuthorName(authorName);
                authorDto.setAuthorInfo(""); // 필요한 경우 추가 정보 설정

                Author author = authorRepository.findByAuthorName(authorName)
                        .map(existingAuthor -> existingAuthor) // 기존 저자를 찾으면 그대로 사용
                        .orElseGet(() -> {
                            Author newAuthor = new Author(authorDto.getAuthorName(), authorDto.getAuthorInfo());
                            return authorRepository.save(newAuthor); // 새 저자를 저장
                        });

                String publisherName = nextLine[3].trim();
                Publisher publisher = publisherRepository.findByPublisherName(publisherName)
                        .orElseGet(() -> publisherRepository.save(new Publisher(publisherName)));

                bookDto.setPublisher(publisher);

                Book bookResult = bookRepository.save(bookDto.convertToEntity());
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
