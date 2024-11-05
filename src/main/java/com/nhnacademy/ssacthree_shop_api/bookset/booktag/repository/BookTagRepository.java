package com.nhnacademy.ssacthree_shop_api.bookset.booktag.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTagId;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookTagRepository extends JpaRepository<BookTag, BookTagId> {
    //todo: custom repository에서 구현해야 할듯
    // 책 id로 책의 tag 이름 조회
    List<Tag> findBookTagsByBookId(Long bookId);
}
