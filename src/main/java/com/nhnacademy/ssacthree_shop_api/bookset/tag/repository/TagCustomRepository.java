package com.nhnacademy.ssacthree_shop_api.bookset.tag.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagCustomRepository {
    Page<TagInfoResponse> findAllTags(Pageable pageable);

    List<TagInfoResponse> findAllTagList();
}
