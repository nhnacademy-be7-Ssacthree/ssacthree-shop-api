package com.nhnacademy.ssacthree_shop_api.bookset.tag.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.request.TagCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.request.TagUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.service.TagService;
import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import java.util.List;

import com.nhnacademy.ssacthree_shop_api.commons.paging.PageRequestBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop/admin/tags")
@RequiredArgsConstructor
public class TagController {
    private static final String TAG_UPDATE_SUCCESS_MESSAGE = "태그 정보 수정 성공";
    private static final String TAG_DELETE_SUCCESS_MESSAGE = "태그 정보 삭제 성공";

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<MessageResponse> tagCreate(@RequestBody TagCreateRequest tagCreateRequest) {
        MessageResponse messageResponse = tagService.saveTag(tagCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @GetMapping
    public ResponseEntity<Page<TagInfoResponse>> getAllTags(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "tagId:asc") String[] sort) {
        Pageable pageable = PageRequestBuilder.createPageable(page, size, sort);
        Page<TagInfoResponse> tags = tagService.getAllTags(pageable);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/lists")
    public ResponseEntity<List<TagInfoResponse>> getAllTagList() {
        return ResponseEntity.ok().body(tagService.getAllTagList());
    }

    @PutMapping
    public ResponseEntity<MessageResponse> updateTag(@RequestBody TagUpdateRequest tagUpdateRequest){
        tagService.updateTag(tagUpdateRequest);
        MessageResponse response = new MessageResponse(TAG_UPDATE_SUCCESS_MESSAGE);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{tag-id}")
    public ResponseEntity<MessageResponse> deleteTag(@PathVariable(name = "tag-id") Long tagId) {
        tagService.deleteTag(tagId);
        MessageResponse response = new MessageResponse(TAG_DELETE_SUCCESS_MESSAGE);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
