package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.service;

import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.PaymentRequest;

public interface PaymentService {
    void savePayment(PaymentRequest paymentRequest);
}
