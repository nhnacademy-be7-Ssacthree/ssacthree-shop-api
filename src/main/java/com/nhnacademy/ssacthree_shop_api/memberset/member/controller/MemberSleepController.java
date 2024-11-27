package com.nhnacademy.ssacthree_shop_api.memberset.member.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberSleepController {

    private final MemberService memberService;


    @PutMapping("/api/shop/member-sleep")
    public ResponseEntity<MessageResponse> activeMember(
        @RequestHeader(name = "memberLoginId") String memberLoginId) {
        
        return ResponseEntity.ok(memberService.activeMember(memberLoginId));
    }
}
