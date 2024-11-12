package com.nhnacademy.ssacthree_shop_api.bookset.tag.service;


import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.request.TagCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import java.util.List;

public interface TagService {

    MessageResponse saveTag(TagCreateRequest tagCreateRequest);

    List<TagInfoResponse> getAllTags();
}
