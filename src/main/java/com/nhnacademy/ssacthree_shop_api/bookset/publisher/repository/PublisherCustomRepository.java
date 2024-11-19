package com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublisherCustomRepository {
    Page<PublisherGetResponse> findAllPublisher(Pageable pageable);
}
