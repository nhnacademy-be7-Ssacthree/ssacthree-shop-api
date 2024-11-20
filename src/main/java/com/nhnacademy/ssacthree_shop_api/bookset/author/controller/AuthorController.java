package com.nhnacademy.ssacthree_shop_api.bookset.author.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.author.service.AuthorService;
import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.commons.paging.PageRequestBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop/admin/authors")
@RequiredArgsConstructor
public class AuthorController {
    public static final String AUTHOR_CREATE_SUCCESS_MESSAGE = "작가 정보 생성 성공";
    public static final String AUTHOR_UPDATE_SUCCESS_MESSAGE = "작가 정보 수정 성공";
    public static final String AUTHOR_DELETE_SUCCESS_MESSAGE = "작가 정보 삭제 성공";

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<Page<AuthorGetResponse>> getAllAuthors(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size,
                                                                 @RequestParam(defaultValue = "authorId:asc") String[] sort) {
        Pageable pageable = PageRequestBuilder.createPageable(page, size, sort);
        Page<AuthorGetResponse> authors = authorService.getAllAuthors(pageable);
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/lists")
    public ResponseEntity<List<AuthorGetResponse>> getAllAuthorList(){
        return ResponseEntity.ok().body(authorService.getAllAuthorList());
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorGetResponse> getAuthor(@PathVariable Long authorId) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAuthorById(authorId));
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createAuthor(
            @Valid @RequestBody AuthorCreateRequest authorCreateRequest) {
        authorService.createAuthor(authorCreateRequest);
        MessageResponse messageResponse = new MessageResponse(AUTHOR_CREATE_SUCCESS_MESSAGE);

        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<MessageResponse> updateAuthor(
            @Valid @RequestBody AuthorUpdateRequest authorUpdateRequest) {
        authorService.updateAuthor(authorUpdateRequest);
        MessageResponse messageResponse = new MessageResponse(AUTHOR_UPDATE_SUCCESS_MESSAGE);

        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<MessageResponse> deleteAuthor(@PathVariable Long authorId){
        authorService.deleteAuthor(authorId);
        MessageResponse messageResponse = new MessageResponse(AUTHOR_DELETE_SUCCESS_MESSAGE);

        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

}
