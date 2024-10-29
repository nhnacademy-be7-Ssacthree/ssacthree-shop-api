package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.service.DeliveryRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/deliveryRules")
@RequiredArgsConstructor
public class DeliveryRuleController {

    private final DeliveryRuleService deliveryRuleService;

    @GetMapping
    public ResponseEntity<List<DeliveryRuleGetResponse>> getAllDeliveryRules() {
        return ResponseEntity.ok().body(deliveryRuleService.getAllDeliveryRules());
    }

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createDeliveryRule(
            @RequestBody DeliveryRuleCreateRequest deliveryRuleCreateRequest) {

        deliveryRuleService.createDeliveryRule(deliveryRuleCreateRequest);
        MessageResponse messageResponse = new MessageResponse("생성 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }
}
