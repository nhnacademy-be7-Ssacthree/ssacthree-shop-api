package com.nhnacademy.ssacthree_shop_api.orderset.order.service.impl;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.repository.DeliveryRuleRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.service.DeliveryRuleService;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderSaveRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.service.OrderService;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.domain.domain.OrderDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final DeliveryRuleRepository deliveryRuleRepository;



    @Override
    @Transactional //하나라도 안되면 롤백필요ㅣ.
    // 주문에 필요한 모든 정보를 넣기. 상세 포인트 쿠폰 등등 모두 !
    public OrderResponse createOrder(OrderSaveRequest orderSaveRequest) {
        // 여기서 주문 하나 저장할때 모든 작업들 함.

        // TODO : 레포에서 이렇게 가져오는게 맞을지 ..
        // 회원 가져옴
        Customer customer = customerRepository.findById(orderSaveRequest.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. ID: " + orderSaveRequest.getCustomerId()));

        // 배송 정책 가져옴
        DeliveryRule deliveryRule = deliveryRuleRepository.findById(orderSaveRequest.getDeliveryRuleId())
                .orElseThrow(() -> new IllegalArgumentException("배송 정책 정보를 찾을 수 없습니다. ID: " + orderSaveRequest.getDeliveryRuleId()));

        // TODO : 주문 정보 생성
        Order order = new Order(
                null,
                customer,
                null,
                LocalDateTime.now(),
                orderSaveRequest.getTotalPrice(),
                orderSaveRequest.getOrderNumber(),
                deliveryRule,
                orderSaveRequest.getRecipientName(),
                orderSaveRequest.getRecipientPhone(),
                orderSaveRequest.getPostalCode(),
                orderSaveRequest.getRoadAddress(),
                orderSaveRequest.getDetailAddress(),
                orderSaveRequest.getOrderRequest(),
                orderSaveRequest.getDeliveryDate()
                );
        orderRepository.save(order); // 주문후 줘야하는 정보.. 상세 ; orderKey랑 결제 key랑 결제 금액

        // TODO : 주문 상세 생성
        OrderDetail orderDetail = new OrderDetail();

        // TODO : 주문 상태 생성

        // TODO : 포장 테이블 생성

        // TODO : 포인트 내역 생성

        // TODO : 포인트, 쿠폰 차감

        // TODO : 재고 차감

        return null;

    }
}
