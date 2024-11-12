package com.nhnacademy.ssacthree_shop_api.bookset.tag.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.request.TagCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.service.TagService;
import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop/admin/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<MessageResponse> tagCreate(@RequestBody TagCreateRequest tagCreateRequest) {
        MessageResponse messageResponse = tagService.saveTag(tagCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @GetMapping
    public ResponseEntity<List<TagInfoResponse>> getAllTags() {
        return ResponseEntity.status(HttpStatus.OK).body(tagService.getAllTags());
    }

}
