package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.controller;

import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.service.DeliveryRuleService;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleGetResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeliveryRuleCustomerController.class)
class DeliveryRuleCustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryRuleService deliveryRuleService;

    @Test
    @DisplayName("고객용 현재 배송 룰 조회 테스트")
    @WithMockUser(username = "testuser", roles = "USER")
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
        mockMvc.perform(get("/api/shop/delivery-rules/current")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryRuleId").value(1L))
                .andExpect(jsonPath("$.deliveryRuleName").value("기본 배송 룰"))
                .andExpect(jsonPath("$.deliveryFee").value(3000))
                .andExpect(jsonPath("$.deliveryDiscountCost").value(500))
                .andExpect(jsonPath("$.deliveryRuleIsSelected").value(true));
    }
}
