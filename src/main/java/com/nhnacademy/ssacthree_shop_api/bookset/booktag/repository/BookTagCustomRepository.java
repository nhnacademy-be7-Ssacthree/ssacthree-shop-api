package com.nhnacademy.ssacthree_shop_api.bookset.booktag.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;

import java.util.List;

public interface BookTagCustomRepository {

    // 책 id로 책의 tag 이름 조회
    List<Tag> findBookTagsByBookId(Long bookId);
}
