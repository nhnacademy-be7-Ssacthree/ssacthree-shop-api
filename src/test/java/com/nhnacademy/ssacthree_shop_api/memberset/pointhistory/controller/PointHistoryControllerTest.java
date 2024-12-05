package com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nhnacademy.ssacthree_shop_api.memberset.member.service.MemberService;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistoryGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.service.PointHistoryService;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PointHistoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class PointHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointHistoryService pointHistoryService;

    @MockBean
    private MemberService memberService;


    @Test
    void getMembersPointHistory() throws Exception {
        // Given
        String memberLoginId = "testUser";
        Long customerId = 1L;
        PointHistoryGetResponse response = new PointHistoryGetResponse(100, LocalDateTime.now(),
            "Purchase");
        Page<PointHistoryGetResponse> mockPage = new PageImpl<>(
            Collections.singletonList(response));

        when(memberService.getCustomerIdByMemberLoginId(memberLoginId)).thenReturn(customerId);
        when(pointHistoryService.getMemberPointHistories(any(Long.class), any(PageRequest.class)))
            .thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/api/shop/members/point-histories")
                .header("X-USER-ID", memberLoginId)
                .param("page", "0")
                .param("size", "5")
                .param("sort", "pointChangeDate")
                .param("direction", "DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
