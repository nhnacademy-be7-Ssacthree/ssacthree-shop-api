package com.nhnacademy.ssacthree_shop_api.bookset.tag.service.impl;

import static com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.QMemberGrade.memberGrade;

import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.request.TagCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.exception.TagAlreadyException;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.repository.TagRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.service.TagService;
import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public MessageResponse saveTag(TagCreateRequest tagCreateRequest) {

        if(tagRepository.existsByTagName(tagCreateRequest.getTagName())) {
            throw new TagAlreadyException("태그가 이미 존재합니다.");
        }

        Tag tag = new Tag(tagCreateRequest.getTagName());
        tagRepository.save(tag);
        MessageResponse messageResponse = new MessageResponse("생성 성공");
        return messageResponse;
    }

    @Override
    public Page<TagInfoResponse> getAllTags(Pageable pageable) {
        return tagRepository.findAllTags(pageable);
    }
}
