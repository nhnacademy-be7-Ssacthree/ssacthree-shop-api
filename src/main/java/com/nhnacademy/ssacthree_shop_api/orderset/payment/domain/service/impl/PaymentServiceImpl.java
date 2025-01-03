package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.service.impl;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.commons.exception.NotFoundException;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.service.OrderService;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.OrderStatus;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.repository.OrderStatusRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.OrderToStatusMapping;
import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.repository.OrderToStatusMappingRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.Payment;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.PaymentStatusEnum;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.PaymentType;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.PaymentCancelRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.PaymentRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.repository.PaymentRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.repository.PaymentTypeRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final OrderService orderService;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderToStatusMappingRepository orderToStatusMappingRepository;

    @Override
    public MessageResponse savePayment(PaymentRequest paymentRequest) {
        Order order = orderService.getOrder(paymentRequest.getOrderId());

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

        try {
            paymentRepository.save(payment);
            log.info("결제 정보 저장 성공: {}", payment);
        } catch (Exception e) {
            log.error("결제 정보 저장 실패", e);
            throw e;
        }
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<String> cancelPayment(Long orderId, PaymentCancelRequest paymentCancelRequest) {
        // 주문 가져오기
        Order order = orderService.getOrder(orderId);

        // 토스페이먼츠 결제 취소

        String url = "https://api.tosspayments.com/v1/payments/" + paymentCancelRequest.getPaymentKey() + "/cancel";

        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizations);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("cancelReason", paymentCancelRequest.getCancelReason());

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            Payment payment = paymentRepository.findByOrderId(orderId).
                    orElseThrow(() -> new NotFoundException("결제 정보가 존재하지 않습니다."));

            // 결제 상태 취소로 변경 - 새로 만들어야하는가 ?
            payment.setPaymentStatus(PaymentStatusEnum.CANCEL);

            // 주문 상태에도 반영
            // TODO : 서비스로 빼기 - cancel
            OrderStatus orderStatus= orderStatusRepository.findById(5L)
                    .orElseThrow(() -> new RuntimeException("상태를 찾을 수 없습니다."));
            OrderToStatusMapping orderToStatusMapping = new OrderToStatusMapping(
                    order,
                    orderStatus,
                    LocalDateTime.now()
            );

            orderToStatusMappingRepository.save(orderToStatusMapping);

            // 포인트, 쿠폰 보상 ?


            return response;
        } catch (HttpClientErrorException.Forbidden ex) {
            System.out.println("API 요청이 거부되었습니다: " + ex.getResponseBodyAsString());
            throw ex;
        }
    }
}
