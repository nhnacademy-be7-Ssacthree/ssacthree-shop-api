package com.nhnacademy.ssacthree_shop_api.memberset.member.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookCommonService;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.request.BookLikeRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.response.BookLikeResponse;
import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.commons.paging.PageRequestBuilder;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberRegisterRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final BookCommonService bookCommonService;

    @PostMapping
    public ResponseEntity<MessageResponse> registerMember(
        @RequestBody MemberRegisterRequest memberRegisterRequest) {

        memberService.registerMember(memberRegisterRequest);
        MessageResponse messageResponse = new MessageResponse("생성 성공");
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteMember(
        @RequestHeader(name = "X-USER-ID") String memberId) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.deleteMember(memberId));
    }


    @GetMapping("/my-page")
    public ResponseEntity<MemberInfoGetResponse> getMemberInfo(
        @RequestHeader(name = "X-USER-ID") String memberId) {

        MemberInfoGetResponse memberInfoGetResponse = memberService.getMemberInfoById(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(memberInfoGetResponse);

    }

    @PutMapping("/my-page")
    public ResponseEntity<MessageResponse> updateMemberInfo(
        @RequestHeader(name = "X-USER-ID") String memberId,
        @RequestBody MemberInfoUpdateRequest memberInfoUpdateRequest) {
        MessageResponse messageResponse = memberService.updateMember(memberId,
            memberInfoUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @GetMapping("/id")
    public ResponseEntity<Long> getCustomerId(@RequestHeader(name = "X-USER-ID") String memberId) {
        Long customerId = memberService.getCustomerIdByMemberLoginId(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(customerId);
    }

    /**
     * 회원의 좋아요 도서 목록을 조회합니다
     * @param page 현재 요청하려는 페이지 번호
     * @param size 한 페이지에 표시할 데이터의 개수
     * @param sort 정렬 조건 (여러개의 정렬 조건을 설정 가능하도록 배열 형태로)
     * @param memberId 회원 로그인 아이디
     * @return 도서 정보 페이지
     */
    @GetMapping("/likes")
    public ResponseEntity<Page<BookListResponse>> getBooksByMemberId(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "bookName:asc") String[] sort,
                                                                     @RequestHeader(name = "X-USER-ID", required = false) String memberId){
        Pageable pageable = PageRequestBuilder.createPageable(page, size, sort);
        Page<BookListResponse> books = bookCommonService.getBooksByMemberId(pageable, memberService.getCustomerIdByMemberLoginId(memberId));
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * 회원의 좋아요 도서 아이디 리스트를 조회합니다.
     * @param memberId 회원 로그인 아이디
     * @return 좋아요 도서 아이디 리스트
     */
    @GetMapping("/likeList")
    public ResponseEntity<List<Long>> getLikedBooksIdForCurrentUser(@RequestHeader(name = "X-USER-ID") String memberId){
        List<Long> bookIdList = bookCommonService.getLikedBooksIdForCurrentUser(memberService.getCustomerIdByMemberLoginId(memberId));
        return new ResponseEntity<>(bookIdList, HttpStatus.OK);
    }

    /**
     * 좋아요를 등록합니다
     * @param request 좋아요 요청
     * @param memberId 회원 로그인 아이디
     * @return 좋아요 정보
     */
    @PostMapping("/likes")
    public ResponseEntity<BookLikeResponse> createBookLikeByMemberId(@RequestBody BookLikeRequest request,
                                                                     @RequestHeader(name = "X-USER-ID") String memberId) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON)
                .body(bookCommonService.saveBookLike(request, memberService.getCustomerIdByMemberLoginId(memberId)));
    }

    /**
     * 좋아요를 삭제합니다.
     * @param bookId 도서 아이디
     * @param memberId 회원 로그인 아이디
     * @return 삭제 성공 여부
     */
    @DeleteMapping("/likes/{book-id}")
    public ResponseEntity<BookLikeResponse> deleteBookLikeByMemberId(@PathVariable(name="book-id") Long bookId,
                                                            @RequestHeader(name = "X-USER-ID") String memberId) {
        BookLikeResponse result = bookCommonService.deleteBookLike(bookId, memberService.getCustomerIdByMemberLoginId(memberId));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
