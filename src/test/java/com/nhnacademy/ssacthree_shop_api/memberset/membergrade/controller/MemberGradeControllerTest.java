package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeUpdateResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.service.MemberGradeService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberGradeController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberGradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberGradeService memberGradeService;

    @Test
    void createMemberGrade() throws Exception {
        MemberGradeCreateRequest request = new MemberGradeCreateRequest("Gold", true, 5.0f);

        doNothing().when(memberGradeService).createMemberGrade(any(MemberGradeCreateRequest.class));

        mockMvc.perform(post("/api/shop/admin/member-grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
    }

    @Test
    void getMemberGrade() throws Exception {
        MemberGradeGetResponse response = new MemberGradeGetResponse(1L, "Gold", true,
            LocalDateTime.now(), 5.0f);

        when(memberGradeService.getMemberGradeById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/shop/admin/member-grades/1"))
            .andExpect(status().isOk());
    }

    @Test
    void getAllMemberGrades() throws Exception {
        List<MemberGradeGetResponse> responses = Arrays.asList(
            new MemberGradeGetResponse(1L, "Gold", true, LocalDateTime.now(), 5.0f),
            new MemberGradeGetResponse(2L, "Silver", true, LocalDateTime.now(), 3.0f)
        );

        when(memberGradeService.getAllMemberGrades()).thenReturn(responses);

        mockMvc.perform(get("/api/shop/admin/member-grades"))
            .andExpect(status().isOk());
    }

    @Test
    void deleteMemberGrade() throws Exception {
        doNothing().when(memberGradeService).deleteMemberGradeById(1L);

        mockMvc.perform(delete("/api/shop/admin/member-grades/1"))
            .andExpect(status().isOk());
    }

    @Test
    void updateMemberGrade() throws Exception {
        MemberGradeUpdateResponse request = new MemberGradeUpdateResponse("Platinum", true, 10.0f);

        doNothing().when(memberGradeService)
            .updateMemberGrade(eq(1L), any(MemberGradeUpdateResponse.class));

        mockMvc.perform(put("/api/shop/admin/member-grades/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }
}
