package com.nhnacademy.ssacthree_shop_api.bookset.booktag.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTagId;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookTagRepository extends JpaRepository<BookTag, BookTagId>, BookTagCustomRepository {
    void deleteAllByTag_TagId(Long tagId);
}
