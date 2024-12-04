package com.nhnacademy.ssacthree_shop_api.bookset.tag.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.booktag.repository.BookTagRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.request.TagCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.request.TagUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.exception.TagAlreadyException;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.exception.TagNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.repository.TagRepository;
import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService; // 테스트 대상 서비스

    @Mock
    private TagRepository tagRepository; // Mock Tag Repository

    @Mock
    private BookTagRepository bookTagRepository; //Mock BookTag Repository

    @Test
    void testSaveTag_success() {
        // Given: 유효한 요청 데이터
        TagCreateRequest request = new TagCreateRequest("newTag");

        // Mock Repository 동작 설정
        Mockito.when(tagRepository.existsByTagName("newTag")).thenReturn(false); // 태그 이름이 존재하지 않음
        Mockito.when(tagRepository.save(Mockito.any(Tag.class))).thenAnswer(invocation -> {
            Tag tag = invocation.getArgument(0);
            tag.setTagId(1L); // 저장된 태그에 ID 할당
            return tag;
        });

        // When: saveTag 호출
        MessageResponse response = tagService.saveTag(request);

        // Then: 결과 검증
        Assertions.assertEquals("생성 성공", response.getMessage());
        Mockito.verify(tagRepository, Mockito.times(1)).save(Mockito.any(Tag.class)); // 저장 메서드 호출 검증
    }

    @Test
    void testSaveTag_tagAlreadyExists() {
        // Given: 이미 존재하는 태그 이름
        TagCreateRequest request = new TagCreateRequest("existingTag");

        // Mock Repository 동작 설정
        Mockito.when(tagRepository.existsByTagName("existingTag")).thenReturn(true); // 태그 이름이 이미 존재

        // When & Then: 예외 발생 검증
        TagAlreadyException exception = Assertions.assertThrows(TagAlreadyException.class,
            () -> tagService.saveTag(request));

        Assertions.assertEquals("태그가 이미 존재합니다.", exception.getMessage());
        Mockito.verify(tagRepository, Mockito.times(0)).save(Mockito.any(Tag.class)); // 저장 메서드 호출되지 않음
    }

    @Test
    void testGetAllTags() {
        // Given: 페이징 요청과 Mock 반환 데이터 준비
        Pageable pageable = PageRequest.of(0, 10); // 첫 번째 페이지, 10개씩
        List<TagInfoResponse> mockTags = Arrays.asList(
            new TagInfoResponse(1L, "Tag1"),
            new TagInfoResponse(2L, "Tag2"),
            new TagInfoResponse(3L, "Tag3")
        );
        Page<TagInfoResponse> mockPage = new PageImpl<>(mockTags, pageable, mockTags.size());

        // Mock Repository 동작 설정
        Mockito.when(tagRepository.findAllTags(pageable)).thenReturn(mockPage);

        // When: getAllTags 호출
        Page<TagInfoResponse> result = tagService.getAllTags(pageable);

        // Then: 결과 검증
        Assertions.assertEquals(3, result.getContent().size()); // 반환된 태그 개수 확인
        Assertions.assertEquals("Tag1", result.getContent().get(0).getTagName()); // 첫 번째 태그 이름 확인
        Mockito.verify(tagRepository, Mockito.times(1)).findAllTags(pageable); // Repository 호출 검증
    }

    @Test
    void testGetAllTagList() {
        // Given: Mock 데이터 준비
        List<TagInfoResponse> mockTagList = Arrays.asList(
            new TagInfoResponse(1L, "Tag1"),
            new TagInfoResponse(2L, "Tag2"),
            new TagInfoResponse(3L, "Tag3")
        );

        // Mock Repository 동작 설정
        Mockito.when(tagRepository.findAllTagList()).thenReturn(mockTagList);

        // When: getAllTagList 호출
        List<TagInfoResponse> result = tagService.getAllTagList();

        // Then: 결과 검증
        Assertions.assertEquals(3, result.size()); // 태그 개수 확인
        Assertions.assertEquals("Tag1", result.get(0).getTagName()); // 첫 번째 태그 이름 확인
        Mockito.verify(tagRepository, Mockito.times(1)).findAllTagList(); // Repository 호출 검증
    }

    @Test
    void testDeleteTag() {
        // Given: 삭제할 태그 ID
        Long tagId = 1L;

        // When: deleteTag 호출
        tagService.deleteTag(tagId);

        // Then: 관련 Repository 메서드 호출 검증
        Mockito.verify(bookTagRepository, Mockito.times(1)).deleteAllByTag_TagId(tagId); // 관련 BookTag 삭제 확인
        Mockito.verify(tagRepository, Mockito.times(1)).deleteById(tagId); // 태그 삭제 확인
    }

    @Test
    void testUpdateTag_success() {
        // Given: 유효한 태그 업데이트 요청
        TagUpdateRequest updateRequest = new TagUpdateRequest(1L, "UpdatedTag");

        // 기존 태그 Mock 데이터
        Tag existingTag = new Tag(1L, "OldTag");

        // Mock Repository 동작 설정
        Mockito.when(tagRepository.existsByTagName("UpdatedTag")).thenReturn(false); // 태그 이름 중복 아님
        Mockito.when(tagRepository.findById(1L)).thenReturn(Optional.of(existingTag)); // 태그 존재
        Mockito.when(tagRepository.save(Mockito.any(Tag.class))).thenReturn(existingTag); // 저장 후 반환

        // When: updateTag 호출
        TagInfoResponse response = tagService.updateTag(updateRequest);

        // Then: 결과 검증
        Assertions.assertEquals("UpdatedTag", response.getTagName()); // 업데이트된 이름 확인
        Mockito.verify(tagRepository, Mockito.times(1)).existsByTagName("UpdatedTag"); // 중복 체크 검증
        Mockito.verify(tagRepository, Mockito.times(1)).findById(1L); // 태그 조회 검증
        Mockito.verify(tagRepository, Mockito.times(1)).save(Mockito.any(Tag.class)); // 저장 검증
    }

    @Test
    void testUpdateTag_tagAlreadyExists() {
        // Given: 중복된 태그 이름 요청
        TagUpdateRequest updateRequest = new TagUpdateRequest(1L, "DuplicateTag");

        // Mock Repository 동작 설정
        Mockito.when(tagRepository.existsByTagName("DuplicateTag")).thenReturn(true); // 태그 이름 중복

        // When & Then: 중복 예외 발생 검증
        TagAlreadyException exception = Assertions.assertThrows(TagAlreadyException.class, () -> {
            tagService.updateTag(updateRequest);
        });

        Assertions.assertEquals("태그가 이미 존재합니다.", exception.getMessage());
        Mockito.verify(tagRepository, Mockito.times(1)).existsByTagName("DuplicateTag"); // 중복 체크 검증
        Mockito.verify(tagRepository, Mockito.times(0)).findById(Mockito.anyLong()); // 태그 조회 호출되지 않음
    }

    @Test
    void testUpdateTag_tagNotFound() {
        // Given: 존재하지 않는 태그 ID 요청
        TagUpdateRequest updateRequest = new TagUpdateRequest(99L, "NonExistingTag");

        // Mock Repository 동작 설정
        Mockito.when(tagRepository.existsByTagName("NonExistingTag")).thenReturn(false); // 태그 이름 중복 아님
        Mockito.when(tagRepository.findById(99L)).thenReturn(Optional.empty()); // 태그 존재하지 않음

        // When & Then: 태그 조회 예외 발생 검증
        TagNotFoundException exception = Assertions.assertThrows(TagNotFoundException.class, () -> {
            tagService.updateTag(updateRequest);
        });

        Assertions.assertEquals("Tag not found", exception.getMessage());
        Mockito.verify(tagRepository, Mockito.times(1)).existsByTagName("NonExistingTag"); // 중복 체크 검증
        Mockito.verify(tagRepository, Mockito.times(1)).findById(99L); // 태그 조회 호출 검증
    }
}
