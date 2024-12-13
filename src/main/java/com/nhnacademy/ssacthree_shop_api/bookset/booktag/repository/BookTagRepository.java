package com.nhnacademy.ssacthree_shop_api.bookset.booktag.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTagId;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookTagRepository extends JpaRepository<BookTag, BookTagId>, BookTagCustomRepository {
    void deleteAllByTag_TagId(Long tagId);
}
