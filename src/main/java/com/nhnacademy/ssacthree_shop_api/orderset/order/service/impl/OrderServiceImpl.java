package com.nhnacademy.ssacthree_shop_api.orderset.order.service.impl;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.repository.DeliveryRuleRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderListResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponseWithCount;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderSaveRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepositoryCustom;
import com.nhnacademy.ssacthree_shop_api.orderset.order.service.OrderService;
import java.time.LocalDateTime;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;
    private final CustomerRepository customerRepository;
    private final DeliveryRuleRepository deliveryRuleRepository;
    private final OrderDetailService orderDetailService;

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

        // TODO 1 : 주문 정보 생성
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

        // TODO : 주문 상세 생성 - 리스트 돌면서 하나씩 생성 .. 응답값 생각하기
        orderDetailService.saveOrderDetails(order, orderSaveRequest.getOrderDetailList());


        // TODO : 주문 상태 생성

        // TODO : 포장 테이블 생성

        // TODO : 포인트 내역 생성

        // TODO : 포인트, 쿠폰 차감

        // TODO : 재고 차감

        return new OrderResponse(order.getId());

    }




    @Override
    public OrderResponseWithCount getOrdersByCustomerAndDate(Long customerId, int page, int size, LocalDateTime startDate, LocalDateTime endDate) {
        Pageable pageable = PageRequest.of(page, size);

        // 주문 조회 (상태 포함)
        Page<OrderListResponse> orderPage = orderRepositoryCustom.findOrdersByCustomerAndDate(customerId, startDate, endDate, pageable);


        orderPage.getContent().forEach(order ->
            log.info("Order ID: {}, Order Date: {}, Total Price: {}, Order Status: {}",
                order.getOrderId(),
                order.getOrderDate(),
                order.getTotalPrice(),
                order.getOrderStatus()));

        // 상태를 포함한 주문 리스트를 생성하여 반환
        return new OrderResponseWithCount(orderPage.getContent(), orderPage.getTotalElements());
    }
}
