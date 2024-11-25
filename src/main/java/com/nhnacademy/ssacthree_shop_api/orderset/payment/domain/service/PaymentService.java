package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.service;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.PaymentRequest;

public interface PaymentService {
    MessageResponse savePayment(PaymentRequest paymentRequest);
}
