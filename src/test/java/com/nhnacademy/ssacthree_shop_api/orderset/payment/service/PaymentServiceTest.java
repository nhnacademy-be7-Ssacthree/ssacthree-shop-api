package com.nhnacademy.ssacthree_shop_api.orderset.payment.service;

import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.service.OrderService;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.repository.OrderStatusRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.OrderToStatusMapping;
import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.repository.OrderToStatusMappingRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.Payment;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.PaymentType;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.PaymentCancelRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.PaymentRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.repository.PaymentRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.repository.PaymentTypeRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentTypeRepository paymentTypeRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderStatusRepository orderStatusRepository;

    @Mock
    private OrderToStatusMappingRepository orderToStatusMappingRepository;

    @Mock
    private RestTemplate restTemplate; // Mock RestTemplate

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("결제 저장 성공 테스트")
    void savePaymentSuccess() {
        // Given
        PaymentRequest paymentRequest = new PaymentRequest(1L, 1L , 10000, "DONE", "payment-key-123", "2024-12-01T10:00:00Z");
        Order order = new Order();
        PaymentType paymentType = new PaymentType();

        when(orderService.getOrder(1L)).thenReturn(order);
        when(paymentTypeRepository.findById(1L)).thenReturn(Optional.of(paymentType));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        paymentService.savePayment(paymentRequest);

        // Then
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("결제 저장 실패 - 결제 타입 유효하지 않음")
    void savePaymentInvalidPaymentType() {
        // Given
        PaymentRequest paymentRequest = new PaymentRequest(1L, 2L,  10000, "DONE", "payment-key-123", "2024-12-01T10:00:00Z");
        when(paymentTypeRepository.findById(2L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> paymentService.savePayment(paymentRequest));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

//    @Test
//    @DisplayName("결제 취소 성공 테스트")
//    void cancelPaymentSuccess() {
//        // Given
//        Long orderId = 1L;
//        PaymentCancelRequest cancelRequest = new PaymentCancelRequest("payment-key-123", "Duplicate order");
//
//        // 결제와 주문 객체 생성
//        Order order = new Order(1L, null, null, LocalDateTime.now(), 10000, "ORD12345", null, "John Doe", "010-1234-5678", "12345", "Road Address", "Detail Address", "No request", null, "Invoice123");
//
//        PaymentType paymentType = new PaymentType(1L, "Credit Card", true, LocalDateTime.now());
//        // Given
//        Payment payment = new Payment(
//                1L,
//                order,
//                paymentType,
//                LocalDateTime.now(),
//                2000,
//                "payment-key-123",
//                PaymentStatusEnum.DONE
//        );
//
//        // Mocking 서비스와 리포지토리
//        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(payment)); // 결제 정보가 존재하도록 설정
//        when(orderStatusRepository.findById(5L)).thenReturn(Optional.of(new OrderStatus()));
//        when(orderToStatusMappingRepository.save(any(OrderToStatusMapping.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Mock 외부 API 호출
//        ResponseEntity<String> mockResponse = new ResponseEntity<>("{\"status\":\"CANCEL\"}", HttpStatus.OK);
//        when(restTemplate.exchange(
//                eq("https://api.tosspayments.com/v1/payments/" + cancelRequest.getPaymentKey() + "/cancel"),
//                eq(HttpMethod.POST),
//                any(HttpEntity.class),
//                eq(String.class)
//        )).thenReturn(mockResponse);
//
//        // When
//        ResponseEntity<String> response = paymentService.cancelPayment(orderId, cancelRequest);
//
//        // Then
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        verify(paymentRepository, times(1)).findByOrderId(orderId);
//        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatusEnum.CANCEL); // 결제 상태가 CANCEL로 변경되었는지 확인
//    }



    @Test
    @DisplayName("결제 취소 실패 - 결제 정보 없음")
    void cancelPaymentNotFound() {
        // Given
        Long orderId = 1L;
        PaymentCancelRequest cancelRequest = new PaymentCancelRequest("payment-key-123", "Duplicate order");
        when(orderService.getOrder(orderId)).thenReturn(new Order());
        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(HttpClientErrorException.class, () -> {
            paymentService.cancelPayment(orderId, cancelRequest);
        });
        verify(orderToStatusMappingRepository, never()).save(any(OrderToStatusMapping.class));
    }
}
