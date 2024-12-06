package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ssacthree_shop_api.config.SecurityConfig;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveType;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.service.PointSaveRuleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PointSaveRuleController.class)
@Import(SecurityConfig.class)
class PointSaveRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PointSaveRuleService pointSaveRuleService;

    @Test
    @DisplayName("모든 포인트 적립 룰 조회 테스트")
    void getAllPointSaveRules() throws Exception {
        // Arrange
        PointSaveRuleGetResponse response = new PointSaveRuleGetResponse(
                1L,
                "적립 룰",
                1000,
                LocalDateTime.now(),
                true,
                PointSaveType.PERCENT
        );
        when(pointSaveRuleService.getAllPointSaveRules()).thenReturn(List.of(response));

        // Act & Assert
        mockMvc.perform(get("/api/shop/admin/point-save-rules")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pointSaveRuleId").value(1L))
                .andExpect(jsonPath("$[0].pointSaveRuleName").value("적립 룰"));
    }

    @Test
    @DisplayName("포인트 적립 룰 생성 테스트")
    void createPointSaveRule() throws Exception {
        // Arrange
        PointSaveRuleCreateRequest request = new PointSaveRuleCreateRequest();
        request.setPointSaveRuleName("포인트 적립 룰");
        request.setPointSaveAmount(1000);
        request.setPointSaveType(PointSaveType.PERCENT);

        // Act & Assert
        mockMvc.perform(post("/api/shop/admin/point-save-rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())  // 201 Created 응답
                .andExpect(jsonPath("$.message").value("생성 성공"));
    }

    @Test
    @DisplayName("포인트 적립 룰 수정 테스트")
    void updatePointSaveRule() throws Exception {
        // Arrange
        PointSaveRuleUpdateRequest request = new PointSaveRuleUpdateRequest(1L);

        // Act & Assert
        mockMvc.perform(put("/api/shop/admin/point-save-rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("수정 성공"));
    }
}
