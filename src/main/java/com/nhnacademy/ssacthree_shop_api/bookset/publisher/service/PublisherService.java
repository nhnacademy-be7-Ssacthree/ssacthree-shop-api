package com.nhnacademy.ssacthree_shop_api.bookset.publisher.service;

import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.CsvProcessingException;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherDto;
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
        List<PublisherDto> publishersToSave = new ArrayList<>();


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

            for (String publisherName : normalizedPublisherNames) {
                if (!existingPublisherNames.contains(publisherName)) {
                    PublisherDto publisherDto = new PublisherDto();
                    publisherDto.setPublisherName(publisherName);
                    publisherDto.setPublisherIsUsed(true);
                    publishersToSave.add(publisherDto);
                }
            }

            if (!publishersToSave.isEmpty()) {
                List<Publisher> publishers = publishersToSave.stream()
                        .map(this::convertToPublisherEntity)
                        .collect(Collectors.toList());
                publisherRepository.saveAll(publishers);
                log.info("출판사 저장에 성공했습니다.:" + publishersToSave);
            } else {
                log.info("저장할 새로운 출판사가 없습니다.");
            }

        }catch (IOException | CsvValidationException e) {
                throw new CsvProcessingException("csv 파일 업로드에 실패했습니다." + e.getMessage());
        }

    }

    private Publisher convertToPublisherEntity(PublisherDto publisherdto){
        return new Publisher(
                publisherdto.getPublisherId(),
                publisherdto.getPublisherName(),
                publisherdto.isPublisherIsUsed()
        );
    }

}
