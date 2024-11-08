package com.nhnacademy.ssacthree_shop_api.bookset.publisher.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherGetResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.service.PublisherService;
import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop/admin/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping
    public ResponseEntity<List<PublisherGetResponse>> getAllPublishers() {
        return ResponseEntity.ok().body(publisherService.getAllPublishers());
    }

    @PutMapping
    ResponseEntity<MessageResponse> updatePublisher(
            @Valid @RequestBody PublisherUpdateRequest publisherUpdateRequest) {

        publisherService.updatePublisher(publisherUpdateRequest);
        MessageResponse messageResponse = new MessageResponse("수정 성공");

        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping
    ResponseEntity<MessageResponse> createPublisher(
            @Valid @RequestBody PublisherCreateRequest publisherCreateRequest) {

        publisherService.createPublisher(publisherCreateRequest);
        MessageResponse messageResponse = new MessageResponse("생성 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }
}
