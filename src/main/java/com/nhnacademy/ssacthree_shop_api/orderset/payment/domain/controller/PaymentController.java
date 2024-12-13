package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.PaymentCancelRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.PaymentRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    ResponseEntity<MessageResponse> savePayment(@RequestBody PaymentRequest paymentRequest) {
        paymentService.savePayment(paymentRequest);
        return ResponseEntity.ok().body(new MessageResponse("결제 성공"));
    }

    @PostMapping("/{order-id}/cancel")
    ResponseEntity<MessageResponse> cancelPayment(@PathVariable(name = "order-id") Long orderId,
                                                  @RequestBody PaymentCancelRequest paymentCancelRequest) {
        paymentService.cancelPayment(orderId, paymentCancelRequest);
        return ResponseEntity.ok().body(new MessageResponse("결제 취소 성공"));
    }
}
