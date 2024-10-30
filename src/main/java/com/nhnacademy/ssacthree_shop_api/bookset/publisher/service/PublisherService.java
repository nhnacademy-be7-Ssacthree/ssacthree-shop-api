package com.nhnacademy.ssacthree_shop_api.bookset.publisher.service;

import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.CsvProcessingException;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherRepository;
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
public class PublisherService {

    @Autowired
    private PublisherRepository publisherRepository;

    public void savePublisherFromCsv(String filePath){
        Set<String> publisherNamesInCsv = new HashSet<>();
        List<Publisher> publishersToSave = new ArrayList<>();


        try{
            CSVParserBuilder parserBuilder = new CSVParserBuilder().withQuoteChar('\''); // 작은따옴표로 감싸진 데이터 처리
            CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(parserBuilder.build())
                .build();

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length > 3 && nextLine[3] != null && !nextLine[3].isEmpty()) {
                    String publisherName = nextLine[3].trim();
                    publisherNamesInCsv.add(publisherName);
                }
            }

            List<String> normalizedPublisherNames = publisherNamesInCsv.stream()
                    .map(String::trim)
                    .toList();

            List<Publisher> existingPublishers = publisherRepository.findAllByPublisherNameIn(normalizedPublisherNames);
            Set<String> existingPublisherNames = existingPublishers.stream()
                    .map(Publisher::getPublisherName)
                    .collect(Collectors.toSet());

            for(String publisherName : normalizedPublisherNames){
                if(!existingPublisherNames.contains(publisherName)){
                    Publisher publisher = new Publisher();
                    publisher.setPublisherName(publisherName);
                    publisher.setPublisherIsUsed(true);
                    publishersToSave.add(publisher);
                }
            }

            if(!publishersToSave.isEmpty()){
                publisherRepository.saveAll(publishersToSave);
                log.info("New publishers saved to database: " + publishersToSave);
            }else{
                log.info("No new publishers saved to database");
            }

        }catch (IOException | CsvValidationException e) {
                throw new CsvProcessingException("Failed to process CSV file" + e.getMessage());
        }

    }

}
