package com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    List<Publisher> findAllByPublisherNameIn(List<String> publisherNames);
    Optional<Publisher> findByPublisherName(String publisherName);
}
