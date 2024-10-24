package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeUpdateResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.service.MemberGradeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/memberGrades")
@RequiredArgsConstructor
public class MemberGradeController {

    private final MemberGradeService memberGradeService;

    @PostMapping
    public ResponseEntity<MessageResponse> createMemberGrade(@RequestBody MemberGradeCreateRequest memberGradeCreateRequest) {
        memberGradeService.createMemberGrade(memberGradeCreateRequest);

        MessageResponse createResponse = new MessageResponse("생성 성공");
        return ResponseEntity.ok().body(createResponse);
    }

    @GetMapping("/{memberGradeId}")
    public ResponseEntity<MemberGradeGetResponse> getMemberGrade(@PathVariable Long memberGradeId) {

        MemberGradeGetResponse memberGradeGetResponse = memberGradeService.getMemberGradeById(memberGradeId);
        return ResponseEntity.ok().body(memberGradeGetResponse);
    }

    @GetMapping
    public ResponseEntity<List<MemberGradeGetResponse>> getAllMemberGrades() {
        return ResponseEntity.ok().body(memberGradeService.getAllMemberGrades());
    }

    @DeleteMapping("/{memberGradeId}")
    public ResponseEntity<MessageResponse> deleteMemberGrade(@PathVariable Long memberGradeId) {
        memberGradeService.deleteMemberGradeById(memberGradeId);
        MessageResponse messageResponse = new MessageResponse(memberGradeId + " - 삭제 완료");
        return ResponseEntity.ok().body(messageResponse);
    }

    @PutMapping("/{memberGradeId}")
    public ResponseEntity<MessageResponse> updateMemberGrade(@PathVariable Long memberGradeId,
        @RequestBody MemberGradeUpdateResponse memberGradeUpdateResponse) {

        memberGradeService.updateMemberGrade(memberGradeId, memberGradeUpdateResponse);
        MessageResponse messageResponse = new MessageResponse(memberGradeId+ "가 수정 되었습니다.");
        return ResponseEntity.ok().body(messageResponse);
    }
}
