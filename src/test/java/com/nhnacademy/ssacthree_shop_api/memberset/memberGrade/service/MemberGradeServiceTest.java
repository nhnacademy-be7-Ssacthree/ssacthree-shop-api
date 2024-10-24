package com.nhnacademy.ssacthree_shop_api.memberset.memberGrade.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeUpdateResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.repository.MemberGradeRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.service.MemberGradeService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



//TODO : 테스트 커버리지가 아예 표시가 안됨.. 왜??? 그러니 내일 해결하고 짜자..
@ExtendWith(MockitoExtension.class)
public class MemberGradeServiceTest {

    @Mock
    private MemberGradeRepository memberGradeRepository;

    @InjectMocks
    private MemberGradeService memberGradeService;


    @Test
    void createMemberGrade() {

        //given
        MemberGradeCreateRequest dummy = new MemberGradeCreateRequest("test",true,0.01f);
        //when
        memberGradeService.createMemberGrade(dummy);
        //then
        verify(memberGradeRepository).save(any(MemberGrade.class));
    }

    @Test
    void updateMemberGrade() {
        // given
        Long memberGradeId = 1L;
        MemberGradeUpdateResponse updateRequest = new MemberGradeUpdateResponse("test2", false, 0.02f);
        MemberGrade existingMemberGrade = new MemberGrade("test1", true, 0.01f);

        when(memberGradeRepository.findById(memberGradeId)).thenReturn(Optional.of(existingMemberGrade));
        when(memberGradeRepository.existsById(memberGradeId)).thenReturn(true);

        // when
        memberGradeService.updateMemberGrade(memberGradeId, updateRequest);

        // then
        verify(memberGradeRepository).save(existingMemberGrade);
    }

    @Test
    void deleteMemberGrade() {

        //given
        Long memberGradeId = 1L;
        MemberGrade existingMemberGrade = new MemberGrade("test1", true, 0.01f);
        when(memberGradeRepository.existsById(memberGradeId)).thenReturn(true);

        //when
        memberGradeService.deleteMemberGradeById(memberGradeId);

        //then
        verify(memberGradeRepository).deleteById(memberGradeId);
    }


}
