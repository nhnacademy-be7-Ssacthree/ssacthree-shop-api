package com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

}
