package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.service.Impl;

import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.Payment;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.PaymentStatusEnum;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.PaymentType;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.PaymentRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.repository.PaymentRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    public void savePayment(PaymentRequest paymentRequest) {
//        Order order = orderRepository.findById(paymentRequest.get)
//
//        Payment payment = new Payment(
//                null,
//                paymentRequest.get)
//
//
//        private Long id;
//        private Order order; // 주문 ID와 연결
//        private PaymentType paymentType;
//        private LocalDateTime paymentCreatedAt;
//        private int paymentAmount;
//        private String paymentKey;
//        private PaymentStatusEnum paymentStatus;

    }
}
