package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeUpdateResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.exception.MemberGradeNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.repository.MemberGradeCustomRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.repository.MemberGradeRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class MemberGradeServiceTest {

    @Mock
    private MemberGradeRepository memberGradeRepository;

    @InjectMocks
    private MemberGradeService memberGradeService;

    @Mock
    private MemberGradeCustomRepository memberGradeCustomRepository;

    @Test
    void createMemberGrade() {

        //given
        MemberGradeCreateRequest dummy = new MemberGradeCreateRequest("test", true, 0.01f);
        //when
        memberGradeService.createMemberGrade(dummy);
        //then
        verify(memberGradeRepository).save(any(MemberGrade.class));
    }


    @Test
    void updateMemberGrade() {
        // given
        Long memberGradeId = 1L;
        MemberGradeUpdateResponse updateRequest = new MemberGradeUpdateResponse("test2", false,
            0.02f);
        MemberGrade existingMemberGrade = new MemberGrade("test1", true, 0.01f);

        when(memberGradeRepository.findById(memberGradeId)).thenReturn(
            Optional.of(existingMemberGrade));

        // when
        memberGradeService.updateMemberGrade(memberGradeId, updateRequest);

        // then
        verify(memberGradeRepository).save(existingMemberGrade);
    }

    @Test
    void updateMemberGradeException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberGradeService.updateMemberGrade(-1L, null);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberGradeService.updateMemberGrade(0L, null);
        });

        when(memberGradeRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(MemberGradeNotFoundException.class, () -> {
            memberGradeService.updateMemberGrade(1L, new MemberGradeUpdateResponse());
        });
    }

    @Test
    void deleteMemberGrade() {

        //given
        Long memberGradeId = 1L;
        MemberGrade existingMemberGrade = new MemberGrade("test1", true, 0.01f);
        when(memberGradeRepository.findById(memberGradeId)).thenReturn(
            Optional.of(existingMemberGrade));

        //when
        memberGradeService.deleteMemberGradeById(memberGradeId);

        //then
        Assertions.assertFalse(existingMemberGrade.isMemberGradeIsUsed());
    }

    @Test
    void deleteMemberGradeException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberGradeService.deleteMemberGradeById(-1L);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberGradeService.deleteMemberGradeById(0L);
        });

        Assertions.assertThrows(MemberGradeNotFoundException.class, () -> {
            when(memberGradeRepository.findById(1L)).thenReturn(Optional.empty());
            memberGradeService.deleteMemberGradeById(1L);
        });
    }


    @Test
    void getMemberGradeById() {
        Long memberGradeId = 1L;
        MemberGrade existingMemberGrade = new MemberGrade("test1", true, 0.01f);
        when(memberGradeRepository.findById(memberGradeId)).thenReturn(
            Optional.of(existingMemberGrade));

        memberGradeService.getMemberGradeById(memberGradeId);
        verify(memberGradeRepository).findById(memberGradeId);
    }

    @Test
    void getMemberGradeByIdException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberGradeService.getMemberGradeById(-1L);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberGradeService.getMemberGradeById(0L);
        });

        Assertions.assertThrows(MemberGradeNotFoundException.class, () -> {
            when(memberGradeRepository.findById(1L)).thenReturn(Optional.empty());
            memberGradeService.deleteMemberGradeById(1L);
        });
    }

    @Test
    void getAllMemberGrades() {
        // given
        List<MemberGradeGetResponse> memberGrades = Arrays.asList(
            new MemberGradeGetResponse(1L, "test1", true, LocalDateTime.now(), 0.01f),
            new MemberGradeGetResponse(2L, "test2", true, LocalDateTime.now(), 0.02f)
        );

        when(memberGradeCustomRepository.findAvailableMemberGrade()).thenReturn(memberGrades);

        // when
        List<MemberGradeGetResponse> result = memberGradeService.getAllMemberGrades();

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("test1", result.get(0).getMemberGradeName());
        Assertions.assertEquals(0.01f, result.get(0).getMemberGradePointSave());

        verify(memberGradeCustomRepository).findAvailableMemberGrade();
    }


}