package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain;

import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.PaymentCancelRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.CancelRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.PaymentCancelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "tossPaymentsClient", url = "https://api.tosspayments.com/v1/payments")
public interface TossPaymentsClient {

    @PostMapping("/{paymentKey}/cancel")
    PaymentCancelResponse cancelPayment(
            @PathVariable("paymentKey") String paymentKey,
            @RequestBody CancelRequest cancelRequest,
            @RequestHeader("Authorization") String authorization,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    );
}
