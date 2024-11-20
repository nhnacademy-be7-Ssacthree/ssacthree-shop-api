package com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PublisherCustomRepository {
    Page<PublisherGetResponse> findAllPublisher(Pageable pageable);

    List<PublisherGetResponse> findAllPublisherList();
}
