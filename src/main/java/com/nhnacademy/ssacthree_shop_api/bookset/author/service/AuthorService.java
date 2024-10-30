package com.nhnacademy.ssacthree_shop_api.bookset.author.service;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.repository.AuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.CsvProcessingException;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public void saveAuthorsFromCsv(String filePath){
        Set<String> authorsNamesInCsv = new HashSet<>();
        List<Author> authorsToSave = new ArrayList<>();

        try{
            CSVParserBuilder parserBuilder = new CSVParserBuilder().withQuoteChar('\'');
            CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(parserBuilder.build())
                .build();

            String[] nextLine;
            while((nextLine = csvReader.readNext()) != null){

                if(nextLine.length > 2 && nextLine[2] != null && !nextLine[2].isEmpty()){
                    String authorName = nextLine[2].trim();
                    authorsNamesInCsv.add(distinguishAuthors(authorName));
                }

            }

            List<String> normalizedAuthorsNames = authorsNamesInCsv.stream()
                .map(String::trim)
                .toList();

            List<Author> existingAuthors = authorRepository.findAllByAuthorNameIn(normalizedAuthorsNames);
            Set<String> existingAuthorsNames = existingAuthors.stream()
                .map(Author::getAuthorName)
                .collect(Collectors.toSet());

            for(String authorName : normalizedAuthorsNames){
                if(!existingAuthorsNames.contains(authorName)){
                    Author author = new Author();
                    author.setAuthorName(authorName);
                    author.setAuthorInfo("");
                    authorsToSave.add(author);
                }
            }

            if(!authorsToSave.isEmpty()){
                authorRepository.saveAll(authorsToSave);
                log.info("New authors saved to databases: " + authorsToSave);
            }else{
                log.info("No new authors saved to databases");
            }


        }catch (IOException | CsvValidationException e) {
            throw new CsvProcessingException("Failed to process CSV file" + e.getMessage());
        }
    }

    protected String distinguishAuthors(String input){
        String[] authorsParts = input.split(",");
        return authorsParts[0].replaceAll("\\(.*?\\)","").trim();
    }

}
