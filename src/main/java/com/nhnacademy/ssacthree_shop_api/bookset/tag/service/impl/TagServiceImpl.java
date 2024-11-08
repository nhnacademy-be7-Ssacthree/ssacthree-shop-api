package com.nhnacademy.ssacthree_shop_api.bookset.tag.service.impl;

import static com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.QMemberGrade.memberGrade;

import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.request.TagCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagGetResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.exception.TagAlreadyException;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.repository.TagRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.service.TagService;
import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeGetResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
    public List<TagGetResponse> getAllTags() {
        List<Tag> tagList = tagRepository.findAll();

        return tagList.stream()
            .map(tag ->
                new TagGetResponse(
                   tag.getTagId(), tag.getTagName()
                )
            ).collect(Collectors.toList());
    }
}
