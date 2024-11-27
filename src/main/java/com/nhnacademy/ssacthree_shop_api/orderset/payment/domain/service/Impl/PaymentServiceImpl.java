package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.service.Impl;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.Payment;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.PaymentStatusEnum;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.PaymentType;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.PaymentRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.repository.PaymentRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.repository.PaymentTypeRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentTypeRepository paymentTypeRepository;

    @Override
    public MessageResponse savePayment(PaymentRequest paymentRequest) {
        // TODO : 적절한 예외 만들기.
        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다. ID: " + paymentRequest.getOrderId()));

        // 결제 타입 존재하면 넣기
        PaymentType paymentType = paymentTypeRepository.findById(paymentRequest.getMethod())
                .orElseThrow(() -> new IllegalArgumentException("결제 타입이 유효하지 않습니다."));
        LocalDateTime approvedAt = ZonedDateTime.parse(paymentRequest.getApprovedAt()).toLocalDateTime();
        PaymentStatusEnum paymentStatusEnum = PaymentStatusEnum.valueOf(paymentRequest.getStatus());

        Payment payment = new Payment(
                null,
                order,
                paymentType,
                approvedAt,
                paymentRequest.getAmount(),
                paymentRequest.getPaymentKey(),
                paymentStatusEnum
        );

        paymentRepository.save(payment);
        return null;
    }
}
