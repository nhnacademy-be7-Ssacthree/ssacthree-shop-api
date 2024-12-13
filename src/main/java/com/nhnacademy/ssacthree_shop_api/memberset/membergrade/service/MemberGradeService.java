package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.service;

import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeUpdateResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.exception.MemberGradeNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.repository.MemberGradeCustomRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.repository.MemberGradeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberGradeService {

    private final MemberGradeRepository memberGradeRepository;
    private final MemberGradeCustomRepository memberGradeCustomRepository;

    private static final String MEMBER_GRADE_ID_NOT_0 = "memberGradeId는 0이하일 수 없습니다.";
    private static final String NOT_FOUND = "를 찾을 수 없습니다.";

    public void createMemberGrade(MemberGradeCreateRequest memberGradeCreateRequest) {

        MemberGrade memberGrade = new MemberGrade(memberGradeCreateRequest.getMemberGradeName(),
            true,
            memberGradeCreateRequest.getMemberGradePointSave());

        memberGradeRepository.save(memberGrade);

    }

    public void updateMemberGrade(Long memberGradeId,
        MemberGradeUpdateResponse memberGradeUpdateResponse) {
        if (memberGradeId <= 0) {
            throw new IllegalArgumentException(MEMBER_GRADE_ID_NOT_0);
        }

        // 찾은 엔티티의 값을 변경해줌
        MemberGrade memberGrade = memberGradeRepository.findById(memberGradeId)
            .orElseThrow(() -> new MemberGradeNotFoundException(memberGradeId + NOT_FOUND));
        memberGrade.setMemberGradeIsUsed(memberGradeUpdateResponse.isMemberGradeIsUsed());
        memberGrade.setMemberGradeName(memberGradeUpdateResponse.getMemberGradeName());
        memberGrade.setMemberGradePointSave(memberGradeUpdateResponse.getMemberGradePointSave());

        memberGradeRepository.save(memberGrade);
    }

    @Transactional(readOnly = true)
    public MemberGradeGetResponse getMemberGradeById(Long memberGradeId) {
        if (memberGradeId <= 0) {
            throw new IllegalArgumentException(MEMBER_GRADE_ID_NOT_0);
        }
        MemberGrade foundMemberGrade = memberGradeRepository.findById(memberGradeId)
            .orElseThrow(() -> new MemberGradeNotFoundException(memberGradeId + NOT_FOUND));
        return new MemberGradeGetResponse(
            foundMemberGrade.getMemberGradeId(),
            foundMemberGrade.getMemberGradeName(),
            foundMemberGrade.isMemberGradeIsUsed(),
            foundMemberGrade.getMemberGradeCreateAt(),
            foundMemberGrade.getMemberGradePointSave()
        );
    }

    public void deleteMemberGradeById(Long memberGradeId) {
        if (memberGradeId <= 0) {
            throw new IllegalArgumentException(MEMBER_GRADE_ID_NOT_0);
        }
        MemberGrade foundMemberGrade = memberGradeRepository.findById(memberGradeId)
            .orElseThrow(() -> new MemberGradeNotFoundException(memberGradeId + NOT_FOUND));
        foundMemberGrade.setMemberGradeIsUsed(false);
    }

    // 등록된 멤버 등급이 없으면 empty한 리스트를 반환 해 줘야해서 따로 예외처리 안함.
    @Transactional(readOnly = true)
    public List<MemberGradeGetResponse> getAllMemberGrades() {
        return memberGradeCustomRepository.findAvailableMemberGrade();
    }

}
