package com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.controller;

import com.nhnacademy.ssacthree_shop_api.memberset.member.service.MemberService;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistoryGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop/members/point-histories")
@RequiredArgsConstructor
public class PointHistoryController {

    private final PointHistoryService pointHistoryService;
    private final MemberService memberService;

    /**
     * 멤버별 PointHistory를 반환합니다. 정렬 기준 필드는 생성 날짜입니다. 생성 날짜를 기준으로 오름차순, 내림차순 정렬 합니다.
     *
     * @param page          페이지
     * @param size          한 페이지에 들어갈 사이즈
     * @param sort          정렬 기준
     * @param direction     정렬 방향 -> 오름차순, 내림차순
     * @param memberLoginId -> 조회할 회원의 로그인 아이디.
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<PointHistoryGetResponse>> getMembersPointHistory(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "pointChangeDate") String sort,
        @RequestParam(defaultValue = "DESC") String direction,
        @RequestHeader(name = "X-USER-ID") String memberLoginId) {

        // 정렬 방향 --> DESC , ASC
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);

        // 포인트 변경 날짜를 기준으로 오름차순 내림차순 정렬
        Sort sortOption = Sort.by(sortDirection, sort);

        //페이저블 객체 생성
        Pageable pageable = PageRequest.of(page, size, sortOption);

        Long customerId = memberService.getCustomerIdByMemberLoginId(memberLoginId);

        Page<PointHistoryGetResponse> pointHistoryGetResponsePage = pointHistoryService.getMemberPointHistories(
            customerId, pageable);
        return ResponseEntity.ok(pointHistoryGetResponsePage);

    }
}
