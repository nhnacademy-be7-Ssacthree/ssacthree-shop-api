package com.nhnacademy.ssacthree_shop_api.bookset.publisher.service;

import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.CsvProcessingException;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.QPublisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherGetResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherUpdateRequest;
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
import java.util.Set;
import java.util.stream.Collectors;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PublisherService {

    @PersistenceContext
    private EntityManager entityManager;

    private final PublisherRepository publisherRepository;

    public Page<PublisherGetResponse> getAllPublishers(Pageable pageable) {
        return publisherRepository.findAllPublisher(pageable);
    }

    public Publisher createPublisher(PublisherCreateRequest publisherCreateRequest) {
        Publisher publisher = new Publisher(
                publisherCreateRequest.getPublisherName()
        );

        return publisherRepository.save(publisher);
    }

    public List<PublisherGetResponse> getAllPublisherList() {
        return publisherRepository.findAllPublisherList();
    }

    public Publisher updatePublisher(PublisherUpdateRequest publisherUpdateRequest) {

        Long publisherId = publisherUpdateRequest.getPublisherId();
        if (publisherId <= 0) {
            throw new IllegalArgumentException("출판사 ID가 잘못되었습니다.");
        }

        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new IllegalArgumentException("출판사가 존재하지 않습니다."));

        publisher.setPublisherIsUsed(!publisher.isPublisherIsUsed());

        return publisherRepository.save(publisher);
    }

    public void savePublisherFromCsv(String filePath){
        Set<String> publisherNamesInCsv = new HashSet<>();
        List<PublisherGetResponse> publishersToSave = new ArrayList<>();


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
                    PublisherGetResponse publisherGetResponse = new PublisherGetResponse();
                    publisherGetResponse.setPublisherName(publisherName);
                    publisherGetResponse.setPublisherIsUsed(true);
                    publishersToSave.add(publisherGetResponse);
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

    private Publisher convertToPublisherEntity(PublisherGetResponse publisherGetResponse){
        return new Publisher(
                publisherGetResponse.getPublisherId(),
                publisherGetResponse.getPublisherName(),
                publisherGetResponse.isPublisherIsUsed()
        );
    }

}
