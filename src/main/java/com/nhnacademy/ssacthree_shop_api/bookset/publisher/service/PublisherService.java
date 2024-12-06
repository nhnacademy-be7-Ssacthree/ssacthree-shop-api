package com.nhnacademy.ssacthree_shop_api.bookset.publisher.service;

import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherGetResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherRepository;
import java.util.List;

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
