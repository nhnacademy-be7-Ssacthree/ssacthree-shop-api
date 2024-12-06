package com.nhnacademy.ssacthree_shop_api.bookset.publisher.service;

import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.CsvProcessingException;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
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

    private Publisher convertToPublisherEntity(PublisherGetResponse publisherGetResponse){
        return new Publisher(
                publisherGetResponse.getPublisherId(),
                publisherGetResponse.getPublisherName(),
                publisherGetResponse.isPublisherIsUsed()
        );
    }
}
