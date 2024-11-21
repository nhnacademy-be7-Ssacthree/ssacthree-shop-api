package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.controller;

import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.service.DeliveryRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop/delivery-rules")
@RequiredArgsConstructor
public class DeliveryRuleCustomerController {

    private final DeliveryRuleService deliveryRuleService;
    @GetMapping("/current")
    ResponseEntity<DeliveryRuleGetResponse> getCurrentDeliveryRule() {
        return ResponseEntity.ok().body(deliveryRuleService.getCurrentDeliveryRule());
    }
}
