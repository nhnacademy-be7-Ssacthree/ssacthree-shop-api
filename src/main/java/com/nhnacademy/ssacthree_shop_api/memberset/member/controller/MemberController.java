package com.nhnacademy.ssacthree_shop_api.memberset.member.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberRegisterRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MessageResponse> registerMember(
        @RequestBody MemberRegisterRequest memberRegisterRequest) {

        memberService.registerMember(memberRegisterRequest);
        MessageResponse messageResponse = new MessageResponse("생성 성공");
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @GetMapping("/mypage")
    public ResponseEntity<MemberGetResponse> getMemberInfo(HttpServletRequest request, @RequestHeader(name = "X-USER-ID") String header) {

        return null;

    }
}
