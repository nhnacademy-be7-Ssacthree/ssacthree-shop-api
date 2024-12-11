package com.nhnacademy.ssacthree_shop_api.orderset.order.service;

import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointorder.domain.PointOrder;
import com.nhnacademy.ssacthree_shop_api.memberset.pointorder.domain.repository.PointOrderRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.exception.PointSaveRuleNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.repository.PointSaveRuleRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.repository.DeliveryRuleRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderDetailSaveRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderListResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponseWithCount;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderSaveRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepositoryCustom;
import com.nhnacademy.ssacthree_shop_api.orderset.order.service.impl.OrderServiceImpl;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service.OrderDetailService;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.service.PointHistoryService;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.OrderStatus;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.repository.OrderStatusRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.OrderToStatusMapping;
import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.repository.OrderToStatusMappingRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Slf4j
class OrderServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private DeliveryRuleRepository deliveryRuleRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailService orderDetailService;

    @Mock
    private OrderToStatusMappingRepository orderToStatusMappingRepository;

    @Mock
    private PointHistoryService pointHistoryService;

    @Mock
    private PointOrderRepository pointOrderRepository;

    @Mock
    private OrderStatusRepository orderStatusRepository;

    @Mock
    private OrderRepositoryCustom orderRepositoryCustom;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PointSaveRuleRepository pointSaveRuleRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
    }

    // 주문 저장 테스트 코드 //
    @Test
    void testCreateOrderWithOrderSaveRequest() {
        // Arrange
        List<OrderDetailSaveRequest> orderDetailList = List.of(
            new OrderDetailSaveRequest(1L, 1L, null, 2, 5000, null),
            new OrderDetailSaveRequest(2L, 1L, null, 1, 10000, null)
        );

        OrderSaveRequest request = new OrderSaveRequest(
            orderDetailList,
            1L,
            "John Buyer",
            "john.buyer@example.com",
            "1234567890",
            "Jane Doe",
            "9876543210",
            "12345",
            "123 Main Street",
            "Apt 101",
            "Leave at the door",
            LocalDate.now().plusDays(2),
            100,
            50,
            20000,
            1L,
            "ORD12345"
        );

        Customer mockCustomer = new Customer();
        DeliveryRule mockDeliveryRule = new DeliveryRule();
        Order mockOrder = new Order(
            1L, // Order ID
            mockCustomer,
            null, // 배송 관련 필드
            LocalDateTime.now(),
            request.getTotalPrice(),
            request.getOrderNumber(),
            mockDeliveryRule,
            request.getRecipientName(),
            request.getRecipientPhone(),
            request.getPostalCode(),
            request.getRoadAddress(),
            request.getDetailAddress(),
            request.getOrderRequest(),
            request.getDeliveryDate(),
            null
        );
        OrderStatus mockOrderStatus = new OrderStatus(); // Mock 객체 생성
        Member mockMember = new Member();
        PointSaveRule mockPointSaveRule = new PointSaveRule();
        
        // 포인트 적립 시 반환 값 만들어주기
        PointHistory mockPointHistory = new PointHistory();
        mockPointHistory.setPointAmount(100); // 적절한 값 설정


        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
        when(deliveryRuleRepository.findById(1L)).thenReturn(Optional.of(mockDeliveryRule));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder); // Mocked save
        when(orderStatusRepository.findById(1L)).thenReturn(Optional.of(mockOrderStatus));              //서비스, orderStatusRepo.findById 수행 시 돌려줄 것
        when(memberRepository.findById(request.getCustomerId())).thenReturn(Optional.of(mockMember));   //서비스, memberRepo.findById 수행 시 돌려줄 것
        when(pointSaveRuleRepository.findPointSaveRuleByPointSaveRuleName("도서 구매 적립"))           //서비스, pointSaveRuleRepository.findPointSaveRuleByPointSaveRuleName 수행 시 돌려줄 것
            .thenReturn(Optional.of(mockPointSaveRule));
        when(pointHistoryService.savePointHistory(any(PointSaveRule.class), any(Member.class), any(PointHistorySaveRequest.class)))
            .thenReturn(mockPointHistory); // savePointHistory.getPointAmount(); 반환 값



        // Act
        OrderResponse response = orderService.createOrder(request);

        // Assert
        assertNotNull(response);
        assertEquals(mockOrder.getId(),
            request.getOrderDetailList().stream()
                .findFirst()
                .map(OrderDetailSaveRequest::getOrderId)
                .orElse(null));        verify(orderRepository, times(1)).save(any(Order.class)); // Verifying that save() was called

        verify(orderDetailService, times(1)).saveOrderDetails(any(Order.class), eq(orderDetailList)); // Verifying order details save
        verify(orderToStatusMappingRepository, times(1)).save(any(OrderToStatusMapping.class)); // Verifying order status save
        verify(pointHistoryService, times(1)).savePointHistory(any(PointSaveRule.class), any(Member.class), any(PointHistorySaveRequest.class)); // Verifying point history save
        verify(pointOrderRepository, times(1)).save(any(PointOrder.class)); // Verifying point order save
        verify(memberRepository, times(1)).findById(request.getCustomerId());

    }

    // 주문 저장 예외 테스트 코드
    @Test
    void testCreateOrderWithCustomerNotFound() {
        // Arrange
        List<OrderDetailSaveRequest> orderDetailList = List.of(
            new OrderDetailSaveRequest(1L, 1L, null, 2, 5000, null)
        );
        OrderSaveRequest request = new OrderSaveRequest(
            orderDetailList,
            1L, "John Buyer", "john.buyer@example.com", "1234567890",
            "Jane Doe", "9876543210", "12345", "123 Main Street",
            "Apt 101", "Leave at the door", LocalDate.now().plusDays(2),
            100, 50, 20000, 1L, "ORD12345"
        );

        // Mock repository behaviors
        when(customerRepository.findById(1L)).thenReturn(Optional.empty()); // 고객이 존재하지 않음

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(request); // 고객이 없으면 예외 발생해야 함
        });
    }

    @Test
    void testCreateOrderWithDeliveryRuleNotFound() {
        // Arrange
        List<OrderDetailSaveRequest> orderDetailList = List.of(
            new OrderDetailSaveRequest(1L, 1L, null, 2, 5000, null)
        );
        OrderSaveRequest request = new OrderSaveRequest(
            orderDetailList,
            1L, "John Buyer", "john.buyer@example.com", "1234567890",
            "Jane Doe", "9876543210", "12345", "123 Main Street",
            "Apt 101", "Leave at the door", LocalDate.now().plusDays(2),
            100, 50, 20000, 1L, "ORD12345"
        );

        // Mock repository behaviors
        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        when(deliveryRuleRepository.findById(1L)).thenReturn(Optional.empty()); // 배송 정책이 존재하지 않음

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(request); // 배송 정책이 없으면 예외 발생해야 함
        });
    }

    @Test
    void testCreateOrderWithOrderStatusNotFound() {
        // Arrange
        List<OrderDetailSaveRequest> orderDetailList = List.of(
            new OrderDetailSaveRequest(1L, 1L, null, 2, 5000, null)
        );
        OrderSaveRequest request = new OrderSaveRequest(
            orderDetailList,
            1L, "John Buyer", "john.buyer@example.com", "1234567890",
            "Jane Doe", "9876543210", "12345", "123 Main Street",
            "Apt 101", "Leave at the door", LocalDate.now().plusDays(2),
            100, 50, 20000, 1L, "ORD12345"
        );

        // Mock repository behaviors
        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        when(deliveryRuleRepository.findById(1L)).thenReturn(Optional.of(new DeliveryRule()));
        when(orderStatusRepository.findById(1L)).thenReturn(Optional.empty()); // 주문 상태가 존재하지 않음

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(request); // 주문 상태가 없으면 예외 발생해야 함
        });
    }

    @Test
    void testCreateOrderWithPointSaveRuleNotFound() {
        // Arrange
        List<OrderDetailSaveRequest> orderDetailList = List.of(
            new OrderDetailSaveRequest(1L, 1L, null, 2, 5000, null)
        );
        OrderSaveRequest request = new OrderSaveRequest(
            orderDetailList,
            1L, "John Buyer", "john.buyer@example.com", "1234567890",
            "Jane Doe", "9876543210", "12345", "123 Main Street",
            "Apt 101", "Leave at the door", LocalDate.now().plusDays(2),
            100, 50, 20000, 1L, "ORD12345"
        );
        Customer mockCustomer = new Customer();
        DeliveryRule mockDeliveryRule = new DeliveryRule();
        Order mockOrder = new Order(
            1L, // Order ID
            mockCustomer,
            null, // 배송 관련 필드
            LocalDateTime.now(),
            request.getTotalPrice(),
            request.getOrderNumber(),
            mockDeliveryRule,
            request.getRecipientName(),
            request.getRecipientPhone(),
            request.getPostalCode(),
            request.getRoadAddress(),
            request.getDetailAddress(),
            request.getOrderRequest(),
            request.getDeliveryDate(),
            null
        );
        Member mockMember = new Member();


        // 순서대로 서비스 코드가 진행되므로 그 전까지의 Mock 설정 다 해줘야합니다.
        // Mock repository behaviors
        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        when(deliveryRuleRepository.findById(1L)).thenReturn(Optional.of(new DeliveryRule()));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder); // Mocked save
        when(memberRepository.findById(request.getCustomerId())).thenReturn(Optional.of(mockMember));   //서비스, memberRepo.findById 수행 시 돌려줄 것
        when(orderStatusRepository.findById(1L)).thenReturn(Optional.of(new OrderStatus()));
        when(pointSaveRuleRepository.findPointSaveRuleByPointSaveRuleName("도서 구매 적립"))
            .thenReturn(Optional.empty());  // 포인트 적립 규칙이 존재하지 않음

        // Act & Assert
        PointSaveRuleNotFoundException exception = assertThrows(PointSaveRuleNotFoundException.class, () -> {
            orderService.createOrder(request); // 포인트 적립 규칙이 없으면 예외 발생해야 함
        });

        // 예외 메시지 확인
        assertEquals("정책이 존재하지 않습니다.", exception.getMessage());
    }

    // END 주문 저장 테스트 코드 //





    @Test
    void testGetOrdersByCustomerAndDate() {
        // Arrange
        Long customerId = 1L;
        int page = 0;
        int size = 10;
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        // Create some mock order responses
        OrderListResponse order1 = new OrderListResponse(1L, LocalDate.now().minusDays(2), 20000, "COMPLETED");
        OrderListResponse order2 = new OrderListResponse(2L, LocalDate.now().minusDays(1), 15000, "IN_SHOPPING");

        // Mock Page behavior
        Page<OrderListResponse> mockPage = new PageImpl<>(List.of(order1, order2));

        // Mock repository
        when(orderRepositoryCustom.findOrdersByCustomerAndDate(eq(customerId), eq(startDate), eq(endDate), any(Pageable.class)))
            .thenReturn(mockPage);

        // Act
        OrderResponseWithCount response = orderService.getOrdersByCustomerAndDate(customerId, page, size, startDate, endDate);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getOrders().size()); // 2 orders should be returned
        assertEquals(2, response.getTotalOrders()); // Total count should be 2
    }

    @Test
    void testGetOrdersByCustomerAndDateWithEmptyResult() {
        // Arrange
        Long customerId = 1L;
        int page = 0;
        int size = 10;
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now().minusDays(7);

        // Mock Page behavior (empty list, no orders)
        Page<OrderListResponse> mockPage = new PageImpl<>(List.of());

        // Mock repository
        when(orderRepositoryCustom.findOrdersByCustomerAndDate(eq(customerId), eq(startDate), eq(endDate), any(
            Pageable.class)))
            .thenReturn(mockPage);

        // Act
        OrderResponseWithCount response = orderService.getOrdersByCustomerAndDate(customerId, page, size, startDate, endDate);

        // Assert
        assertNotNull(response);
        assertTrue(response.getOrders().isEmpty()); // No orders should be returned
        assertEquals(0, response.getTotalOrders()); // Total count should be 0
    }

    @Test
    void testGetOrdersByCustomerAndDateWithInvalidPageSize() {
        // Arrange
        Long customerId = 1L;
        int page = 0;
        int size = 0;  // Invalid page size (should be >= 1)
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.getOrdersByCustomerAndDate(customerId, page, size, startDate, endDate);
        });

        assertEquals("Page size must not be less than one", exception.getMessage()); // Assert the exception message
    }





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

