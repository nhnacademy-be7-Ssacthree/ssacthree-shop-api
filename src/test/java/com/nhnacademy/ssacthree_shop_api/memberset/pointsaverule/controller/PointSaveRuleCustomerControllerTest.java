package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.controller;

import com.nhnacademy.ssacthree_shop_api.config.SecurityConfig;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleInfoResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.service.PointSaveRuleService;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PointSaveRuleCustomerController.class)
@Import(SecurityConfig.class)
class PointSaveRuleCustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointSaveRuleService pointSaveRuleService;

    @Test
    @DisplayName("포인트 저장 규칙 이름으로 조회 테스트")
    void getPointSaveRuleByRuleName() throws Exception {
        // Arrange
        String ruleName = "포인트 적립 규칙";
        PointSaveRule pointSaveRule = new PointSaveRule(ruleName, 100, PointSaveType.PERCENT);
        PointSaveRuleInfoResponse response = new PointSaveRuleInfoResponse(pointSaveRule);

        when(pointSaveRuleService.getPointSaveRuleByRuleName(ruleName)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/shop/point-save-rules/{point-save-rule-name}", ruleName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pointSaveRuleName").value(ruleName))
                .andExpect(jsonPath("$.pointSaveAmount").value(100))
                .andExpect(jsonPath("$.pointSaveType").value("PERCENT"));
    }
}
