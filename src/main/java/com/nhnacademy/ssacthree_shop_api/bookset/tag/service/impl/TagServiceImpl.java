package com.nhnacademy.ssacthree_shop_api.bookset.tag.service.impl;

import static com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.QMemberGrade.memberGrade;

import com.nhnacademy.ssacthree_shop_api.bookset.booktag.repository.BookTagRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.request.TagCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.request.TagUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.exception.TagAlreadyException;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.exception.TagNotFoundException;
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
    private static final String TAG_EXIST_ERROR_MESSAGE = "태그가 이미 존재합니다.";

    private final TagRepository tagRepository;
    private final BookTagRepository bookTagRepository;

    @Override
    public MessageResponse saveTag(TagCreateRequest tagCreateRequest) {

        if(tagRepository.existsByTagName(tagCreateRequest.getTagName())) {
            throw new TagAlreadyException(TAG_EXIST_ERROR_MESSAGE);
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

    @Override
    public List<TagInfoResponse> getAllTagList(){
        return tagRepository.findAllTagList();
    }

    @Override
    public TagInfoResponse updateTag(TagUpdateRequest tagUpdateRequest) {

        // 태그 이름이 이미 존재하는지 확인 (동일한 태그 이름을 가진 다른 태그가 존재할 때 예외 발생)
        if (tagRepository.existsByTagName(tagUpdateRequest.getTagName())) {
            throw new TagAlreadyException(TAG_EXIST_ERROR_MESSAGE);
        }

        // 기존 태그 조회
        Tag tag = tagRepository.findById(tagUpdateRequest.getTagId())
            .orElseThrow(() -> new TagNotFoundException("Tag not found"));

        // 태그 이름 수정
        tag.setTagName(tagUpdateRequest.getTagName());

        // 수정된 태그 저장
        tagRepository.save(tag);

        return new TagInfoResponse(tag);
    }

    @Override
    public void deleteTag(Long tagId){
        bookTagRepository.deleteAllByTag_TagId(tagId);

        tagRepository.deleteById(tagId);
    }
}
