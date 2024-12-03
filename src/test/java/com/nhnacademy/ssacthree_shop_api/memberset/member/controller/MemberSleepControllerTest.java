package com.nhnacademy.ssacthree_shop_api.memberset.member.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class MemberSleepControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberSleepController memberSleepController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void activeMember_ShouldReturnMessageResponse() {
        // Arrange
        String memberLoginId = "testUser";
        MessageResponse mockResponse = new MessageResponse("휴면 해제가 완료되었습니다.");
        when(memberService.activeMember(memberLoginId)).thenReturn(mockResponse);

        // Act
        ResponseEntity<MessageResponse> response = memberSleepController.activeMember(
            memberLoginId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(memberService, times(1)).activeMember(memberLoginId);
    }
}
