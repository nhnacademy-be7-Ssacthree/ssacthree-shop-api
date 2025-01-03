package com.nhnacademy.ssacthree_shop_api.orderset.order.service.impl;

import com.nhnacademy.ssacthree_shop_api.commons.exception.NotFoundException;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.service.PointHistoryService;
import com.nhnacademy.ssacthree_shop_api.memberset.pointorder.domain.PointOrder;
import com.nhnacademy.ssacthree_shop_api.memberset.pointorder.domain.repository.PointOrderRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.exception.PointSaveRuleNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.repository.PointSaveRuleRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.repository.DeliveryRuleRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.*;
import com.nhnacademy.ssacthree_shop_api.orderset.order.exception.NotFoundOrderException;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepositoryCustom;
import com.nhnacademy.ssacthree_shop_api.orderset.order.service.OrderService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service.OrderDetailService;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.OrderStatus;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.repository.OrderStatusRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.OrderStatusEnum;
import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.OrderToStatusMapping;
import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.repository.OrderToStatusMappingRepository;
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
    private final OrderStatusRepository orderStatusRepository;
    private final OrderToStatusMappingRepository orderToStatusMappingRepository;
    private final PointHistoryService pointHistoryService;
    private final PointSaveRuleRepository pointSaveRuleRepository;
    private final MemberRepository memberRepository;
    private final PointOrderRepository pointOrderRepository;

    @Override
    @Transactional //하나라도 안되면 롤백필요ㅣ.
    // 주문에 필요한 모든 정보를 넣기. 상세 포인트 쿠폰 등등 모두 !
    public OrderResponse createOrder(OrderSaveRequest orderSaveRequest) {
        // 여기서 주문 하나 저장할때 모든 작업들 함.

        // TODO : 레포에서 이렇게 가져오는게 맞을지 .. 서비스로 빼고 서비스단 이용하도록 추후 수정.
        // 회원 가져옴
        Customer customer = customerRepository.findById(orderSaveRequest.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("고객 정보를 찾을 수 없습니다. ID: " + orderSaveRequest.getCustomerId()));

        // 배송 정책 가져옴
        DeliveryRule deliveryRule = deliveryRuleRepository.findById(orderSaveRequest.getDeliveryRuleId())
                .orElseThrow(() -> new IllegalArgumentException("배송 정책 정보를 찾을 수 없습니다. ID: " + orderSaveRequest.getDeliveryRuleId()));

        // TODO 주문 정보 생성
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
                orderSaveRequest.getDeliveryDate(),
                null
        );
        try {
            orderRepository.save(order); // 주문후 줘야하는 정보.. 상세 ; orderKey랑 결제 key랑 결제 금액
            log.info("주문 저장 성공: {}", order);
        } catch (Exception e) {
            log.error("주문 저장 실패", e);
            throw e;
        }

        // TODO : 주문 상세 생성 - 리스트 돌면서 하나씩 생성 .. 응답값 생각하기, 최대한 db접근 최소화
        try {
            orderDetailService.saveOrderDetails(order, orderSaveRequest.getOrderDetailList());
        } catch (Exception e) {
            throw new RuntimeException("주문상세 정보 저장에 실패했습니다." + e.getMessage());
        }

        // TODO : 주문 완료시 상태 생성 - 결제 완료, 대기
        OrderStatus orderStatus= orderStatusRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("상태를 찾을 수 없습니다."));
        OrderToStatusMapping orderToStatusMapping = new OrderToStatusMapping(
                order,
                orderStatus,
                LocalDateTime.now()
        );

        try {
            orderToStatusMappingRepository.save(orderToStatusMapping);
            log.info("주문 상태 저장 성공: {}", orderToStatusMapping);
        } catch (Exception e) {
            log.error("주문 상태 저장 실패", e);
            throw e;
        }

        // TODO : 포인트 적립, 사용 내역 생성 - 포인트 서비스 - 하나로 묶기 ?
        Optional<Member> optionalMember = memberRepository.findById(orderSaveRequest.getCustomerId());

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            int pointHistory = 0;
            PointSaveRule pointSaveRule = pointSaveRuleRepository.findPointSaveRuleByPointSaveRuleName("도서 구매 적립")
                    .orElseThrow(() -> new PointSaveRuleNotFoundException("정책이 존재하지 않습니다."));

            PointHistory savePointHistory = pointHistoryService.savePointHistory(
                    pointSaveRule,
                    member,
                    new PointHistorySaveRequest(orderSaveRequest.getPointToSave(), pointSaveRule.getPointSaveRuleName()));
            pointOrderRepository.save(new PointOrder(savePointHistory, order));
            log.info("포인트 적립 성공");
            pointHistory += savePointHistory.getPointAmount();

            if (0 < orderSaveRequest.getPointToUse() && orderSaveRequest.getPointToUse() <= member.getMemberPoint() ) {
                PointHistory usePointHistory = pointHistoryService.savePointHistory(
                        null,
                        member,
                        new PointHistorySaveRequest((-1) * orderSaveRequest.getPointToUse(), "도서 포인트 결제")
                );
                pointOrderRepository.save(new PointOrder(usePointHistory, order));
                log.info("포인트 결제 성공");
                pointHistory += usePointHistory.getPointAmount();
            }
            member.setMemberPoint(member.getMemberPoint() + pointHistory);
        }

        // TODO : 회원 쿠폰 차감 - 쿠폰 서비스에 구현 : 도서(상세) or 주문에 적용 쿠폰

        // TODO : 장바구니 비우기



        return new OrderResponse(order.getId());

    }

    @Override
    public OrderResponseWithCount getOrdersByCustomerAndDate(Long customerId, int page, int size, LocalDateTime startDate, LocalDateTime endDate) {
        Pageable pageable = PageRequest.of(page, size);
        if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one");
        }
        // 주문 조회 (상태 포함)
        Page<OrderListResponse> orderPage = orderRepositoryCustom.findOrdersByCustomerAndDate(customerId, startDate, endDate, pageable);

        // orderPage가 null일 경우를 방어적으로 처리
        if (orderPage == null || orderPage.isEmpty()) {
            return new OrderResponseWithCount(List.of(), 0);  // 빈 리스트와 0개 카운트를 반환
        }

        orderPage.getContent().forEach(order ->
                log.info("Order ID: {}, Order Date: {}, Total Price: {}, Order Status: {}",
                        order.getOrderId(),
                        order.getOrderDate(),
                        order.getTotalPrice(),
                        order.getOrderStatus()));

        // 상태를 포함한 주문 리스트를 생성하여 반환
        return new OrderResponseWithCount(orderPage.getContent(), orderPage.getTotalElements());
    }



    @Override
    public AdminOrderResponseWithCount adminGetAllOrders(int page, int size, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Pageable pageable = PageRequest.of(page, size);

        // 주문 조회 (상태 포함)
        Page<AdminOrderListResponse> orderPage = orderRepositoryCustom.adminFindAllOrders(startDateTime, endDateTime, pageable);


        orderPage.getContent().forEach(order ->
                log.info("Order ID: {}, Order Date: {}, Total Price: {}, Order Status: {}",
                        order.getOrderId(),
                        order.getOrderDate(),
                        order.getTotalPrice(),
                        order.getOrderStatus(),
                        order.getCustomerName(),
                        order.getOrderNumber()));

        // 상태를 포함한 주문 리스트를 생성하여 반환
        return new AdminOrderResponseWithCount(orderPage.getContent(), orderPage.getTotalElements());
    }

    @Override
    public boolean updateOrderStatus(Long orderId, String status) {
        // 주문의 제일 최신 상태 가져오기
        Optional<OrderToStatusMapping> order = orderToStatusMappingRepository.findByOrderIdOrderByOrderStatusCreatedAtDesc(Long.valueOf(orderId), PageRequest.of(0, 1)).stream().findFirst();
        if (Objects.isNull(order) || order.isEmpty()) {
            throw new NotFoundOrderException("주문을 찾을 수 없습니다.");
        }

        OrderStatusEnum orderStatus = order.get().getOrderStatus().getOrderStatusEnum();

        if (status.equals("start")) {
            // 제일 최신이 대기중이 맞다면, 배송중으로 하나 더 생성
            if (orderStatus == OrderStatusEnum.PENDING) {
                Optional<OrderStatus> newOrderStatus = orderStatusRepository.findById(Long.valueOf(2));
                OrderToStatusMapping orderToStatusMapping = new OrderToStatusMapping(
                        order.get().getOrder(),
                        newOrderStatus.get(),
                        LocalDateTime.now()
                );

                // 운송장 번호 발급
                String uuid = UUID.randomUUID().toString();
                String randomPart = uuid.replaceAll("[^a-zA-Z0-9]", "").substring(0, 9).toUpperCase();
                Optional<Order> targetOrder = orderRepository.findById(orderId);
                targetOrder.get().setInvoice_number("INV" + randomPart);

                orderToStatusMappingRepository.save(orderToStatusMapping);
                return true;
            }
            throw new RuntimeException("이미 배송 중인 주문입니다.");
        } else {
            if (orderStatus == OrderStatusEnum.IN_SHOPPING) {
                Optional<OrderStatus> newOrderStatus = orderStatusRepository.findById(Long.valueOf(3));
                OrderToStatusMapping orderToStatusMapping = new OrderToStatusMapping(
                        order.get().getOrder(),
                        newOrderStatus.get(),
                        LocalDateTime.now()
                );
                orderToStatusMappingRepository.save(orderToStatusMapping);
                return true;
            }
            throw new RuntimeException("이미 배송 완료인 주문입니다.");
        }
    }


    // OrderId로 order 객체 반환
    @Override
    public Order getOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            return optionalOrder.get();
        } else {
            throw new NotFoundException("해당 주문이 존재하지 않습니다.");
        }
    }
}