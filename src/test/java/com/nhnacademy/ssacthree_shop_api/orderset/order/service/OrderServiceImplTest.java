package com.nhnacademy.ssacthree_shop_api.orderset.order.service;

import com.nhnacademy.ssacthree_shop_api.commons.exception.NotFoundException;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointorder.domain.repository.PointOrderRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.repository.DeliveryRuleRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderListResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponseWithCount;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderSaveRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.exception.NotFoundOrderException;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepositoryCustom;
import com.nhnacademy.ssacthree_shop_api.orderset.order.service.impl.OrderServiceImpl;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service.OrderDetailService;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.OrderStatus;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.repository.OrderStatusRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.OrderToStatusMapping;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.service.PointHistoryService;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.repository.PointSaveRuleRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.repository.OrderToStatusMappingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Slf4j
@ExtendWith(MockitoExtension.class) // Mockito를 활성화
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderRepositoryCustom orderRepositoryCustom;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private DeliveryRuleRepository deliveryRuleRepository;

    @Mock
    private OrderDetailService orderDetailService;

    @Mock
    private OrderStatusRepository orderStatusRepository;

    @Mock
    private OrderToStatusMappingRepository orderToStatusMappingRepository;

    @Mock
    private PointHistoryService pointHistoryService;

    @Mock
    private PointSaveRuleRepository pointSaveRuleRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private EntityManager entityManager;
    private Long customerId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int page;
    private int size;

    private Customer mockCustomer;
    private Order mockOrder1;
    private Order mockOrder2;
    private Pageable pageable;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mock 객체 초기화
        customerId = 1L;
        startDate = LocalDateTime.of(2024, 12, 1, 0, 0, 0, 0);
        endDate = LocalDateTime.of(2024, 12, 10, 23, 59, 59, 999999);
        page = 0;
        size = 10;
        // mock Pageable 객체 생성
        pageable = PageRequest.of(0, 10);

        mockCustomer = mock(Customer.class);

    }


//    @Test
//    void getOrdersByCustomerAndDateTest() {
//        // given
//        Long customerId = 1L;
//        LocalDateTime startDate = LocalDateTime.of(2024, 12, 1, 0, 0, 0);
//        LocalDateTime endDate = LocalDateTime.of(2024, 12, 5, 23, 59, 59);
//        int page = 0;
//        int size = 10;
//        Pageable pageable = PageRequest.of(page, size);
//
//        // Mock 데이터 설정
//        OrderListResponse orderResponse1 = new OrderListResponse(1L, LocalDate.of(2024, 12, 1), 10000, "PENDING");
//        OrderListResponse orderResponse2 = new OrderListResponse(2L, LocalDate.of(2024, 12, 4), 15000, "COMPLETED");
//        Page<OrderListResponse> mockPage = new PageImpl<>(List.of(orderResponse1, orderResponse2), pageable, 2); // Page 설정
//
//        // Mock 동작 설정: 실제 Repository 호출이 있을 때 mockPage를 반환하도록 설정
//        when(orderRepositoryCustom.findOrdersByCustomerAndDate(eq(customerId), eq(startDate), eq(endDate), any(Pageable.class)))
//                .thenReturn(mockPage);
//
//        // when
//        OrderResponseWithCount result = orderService.getOrdersByCustomerAndDate(customerId, page, size, startDate, endDate);
//
//        // then
//        assertNotNull(result);
//        assertEquals(2, result.getTotalOrders());  // Mock 데이터에 맞춰서 검증
//        assertEquals(2, result.getOrders().size());  // Mock 데이터에 맞춰서 검증
//
//        // Mock 데이터 출력
//        log.info("Mock 데이터 총 주문 수: {}", result.getTotalOrders());
//        log.info("Mock 데이터 주문 리스트 크기: {}", result.getOrders().size());
//    }





    @Test
    void createOrderWhenCustomerNotFound() {
        // Customer가 없는 경우 예외가 발생하는지 확인하는 테스트
        OrderSaveRequest orderSaveRequest = new OrderSaveRequest();
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(orderSaveRequest);
        });
    }

    @Test
    void createOrderWhenDeliveryRuleNotFound() {
        // DeliveryRule이 없는 경우 예외가 발생하는지 확인하는 테스트
        OrderSaveRequest orderSaveRequest = new OrderSaveRequest();
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(mock(Customer.class)));
        when(deliveryRuleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(orderSaveRequest);
        });
    }
}

