package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.service;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.PaymentCancelRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.PaymentRequest;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    MessageResponse savePayment(PaymentRequest paymentRequest);

    ResponseEntity<String> cancelPayment(Long paymentKey, PaymentCancelRequest cancelReason);
}
