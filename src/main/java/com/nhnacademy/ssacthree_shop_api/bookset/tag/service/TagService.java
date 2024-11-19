package com.nhnacademy.ssacthree_shop_api.bookset.tag.service;


import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.request.TagCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    MessageResponse saveTag(TagCreateRequest tagCreateRequest);

    Page<TagInfoResponse> getAllTags(Pageable pageable);
}
