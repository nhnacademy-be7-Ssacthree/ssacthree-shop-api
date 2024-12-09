package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ssacthree_shop_api.config.SecurityConfig;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.service.DeliveryRuleService;
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

@WebMvcTest(DeliveryRuleController.class)
@Import(SecurityConfig.class)
class DeliveryRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeliveryRuleService deliveryRuleService;

    @Test
    @DisplayName("모든 배송 룰 조회 테스트")
    void getAllDeliveryRules() throws Exception {
        // Arrange
        DeliveryRuleGetResponse response = new DeliveryRuleGetResponse(
                1L,
                "기본 배송 룰",
                3000,
                500,
                true,
                LocalDateTime.now()
        );
        when(deliveryRuleService.getAllDeliveryRules()).thenReturn(List.of(response));

        // Act & Assert
        mockMvc.perform(get("/api/shop/admin/delivery-rules")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].deliveryRuleId").value(1L))
                .andExpect(jsonPath("$[0].deliveryRuleName").value("기본 배송 룰"));
    }

    @Test
    @DisplayName("배송 룰 생성 테스트")
    void createDeliveryRule() throws Exception {
        // Arrange
        DeliveryRuleCreateRequest request = new DeliveryRuleCreateRequest();
        request.setDeliveryRuleName("새 배송 룰");
        request.setDeliveryFee(3000);
        request.setDeliveryDiscountCost(500);

        // Act & Assert
        mockMvc.perform(post("/api/shop/admin/delivery-rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())  // 201 Created 응답
                .andExpect(jsonPath("$.message").value("생성 성공"));
    }

    @Test
    @DisplayName("배송 룰 수정 테스트")
    void updateDeliveryRule() throws Exception {
        // Arrange
        DeliveryRuleUpdateRequest request = new DeliveryRuleUpdateRequest(1L);

        // Act & Assert
        mockMvc.perform(put("/api/shop/admin/delivery-rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("수정 성공"));
    }

    @Test
    @DisplayName("현재 배송 룰 조회 테스트")
    void getCurrentDeliveryRule() throws Exception {
        // Arrange
        DeliveryRuleGetResponse response = new DeliveryRuleGetResponse(
                1L,
                "기본 배송 룰",
                3000,
                500,
                true,
                LocalDateTime.now()
        );
        when(deliveryRuleService.getCurrentDeliveryRule()).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/shop/admin/delivery-rules/current")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryRuleId").value(1L))
                .andExpect(jsonPath("$.deliveryRuleName").value("기본 배송 룰"))
                .andExpect(jsonPath("$.deliveryFee").value(3000))
                .andExpect(jsonPath("$.deliveryDiscountCost").value(500))
                .andExpect(jsonPath("$.deliveryRuleIsSelected").value(true));
    }
}
