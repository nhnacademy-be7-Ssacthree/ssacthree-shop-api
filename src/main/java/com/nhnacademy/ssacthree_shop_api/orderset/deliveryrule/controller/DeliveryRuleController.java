package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.service.DeliveryRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop/admin/delivery-rules")
@RequiredArgsConstructor
public class DeliveryRuleController {

    private final DeliveryRuleService deliveryRuleService;

    @GetMapping
    public ResponseEntity<List<DeliveryRuleGetResponse>> getAllDeliveryRules() {
        return ResponseEntity.ok().body(deliveryRuleService.getAllDeliveryRules());
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createDeliveryRule(
            @Valid @RequestBody DeliveryRuleCreateRequest request) {

        deliveryRuleService.createDeliveryRule(request);
        MessageResponse messageResponse = new MessageResponse("생성 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @PutMapping
    public ResponseEntity<MessageResponse> updateDeliveryRule(
            @Valid @RequestBody DeliveryRuleUpdateRequest deliveryRuleUpdateRequest) {

        deliveryRuleService.updateDeliveryRule(deliveryRuleUpdateRequest);
        MessageResponse messageResponse = new MessageResponse("수정 성공");

        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @GetMapping("/current")
    ResponseEntity<DeliveryRuleGetResponse> getCurrentDeliveryRule() {
        return ResponseEntity.ok().body(deliveryRuleService.getCurrentDeliveryRule());
    }
}
