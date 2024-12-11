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
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.AdminOrderListResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.AdminOrderResponseWithCount;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderDetailSaveRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderListResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponseWithCount;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderSaveRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.order.exception.NotFoundOrderException;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepositoryCustom;
import com.nhnacademy.ssacthree_shop_api.orderset.order.service.impl.OrderServiceImpl;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service.OrderDetailService;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.service.PointHistoryService;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.OrderStatus;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.repository.OrderStatusRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.OrderStatusEnum;
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




    // AdminGetALlOrders 테스트 //
    @Test
    void testAdminGetAllOrders() {
        // Arrange
        int page = 0;
        int size = 10;
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(30);
        LocalDateTime endDateTime = LocalDateTime.now();

        // Create mock responses with the updated AdminOrderListResponse
        AdminOrderListResponse order1 = new AdminOrderListResponse(
            1L,
            LocalDate.now().minusDays(5),
            20000,
            "COMPLETED",
            LocalDate.now().minusDays(5),
            "John Buyer",
            "ORD123",
            "INV123"
        );

        AdminOrderListResponse order2 = new AdminOrderListResponse(
            2L,
            LocalDate.now().minusDays(3),
            15000,
            "SHIPPED",
            LocalDate.now().minusDays(3),
            "Jane Smith",
            "ORD124",
            "INV124"
        );

        // Mock Page behavior with a list of orders
        Page<AdminOrderListResponse> mockPage = new PageImpl<>(List.of(order1, order2));

        // Mock repository
        when(orderRepositoryCustom.adminFindAllOrders(eq(startDateTime), eq(endDateTime), any(Pageable.class)))
            .thenReturn(mockPage);

        // Act
        AdminOrderResponseWithCount response = orderService.adminGetAllOrders(page, size, startDateTime, endDateTime);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getOrders().size()); // 2 orders should be returned
        assertEquals(2, response.getTotalOrders()); // Total count should be 2

        // Assert the order details
        AdminOrderListResponse firstOrder = response.getOrders().getFirst();
        assertEquals(1L, firstOrder.getOrderId());
        assertEquals(LocalDate.now().minusDays(5), firstOrder.getOrderDate());
        assertEquals(20000, firstOrder.getTotalPrice());
        assertEquals("COMPLETED", firstOrder.getOrderStatus());
        assertEquals("John Buyer", firstOrder.getCustomerName());
        assertEquals("ORD123", firstOrder.getOrderNumber());
        assertEquals("INV123", firstOrder.getInvoiceNumber());

        AdminOrderListResponse secondOrder = response.getOrders().get(1);
        assertEquals(2L, secondOrder.getOrderId());
        assertEquals(LocalDate.now().minusDays(3), secondOrder.getOrderDate());
        assertEquals(15000, secondOrder.getTotalPrice());
        assertEquals("SHIPPED", secondOrder.getOrderStatus());
        assertEquals("Jane Smith", secondOrder.getCustomerName());
        assertEquals("ORD124", secondOrder.getOrderNumber());
        assertEquals("INV124", secondOrder.getInvoiceNumber());
    }

    @Test
    void testAdminGetAllOrdersWithEmptyResult() {
        // Arrange
        int page = 0;
        int size = 10;
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(30);
        LocalDateTime endDateTime = LocalDateTime.now().minusDays(10);

        // Mock Page behavior (empty list, no orders)
        Page<AdminOrderListResponse> mockPage = new PageImpl<>(List.of());

        // Mock repository
        when(orderRepositoryCustom.adminFindAllOrders(eq(startDateTime), eq(endDateTime), any(Pageable.class)))
            .thenReturn(mockPage);

        // Act
        AdminOrderResponseWithCount response = orderService.adminGetAllOrders(page, size, startDateTime, endDateTime);

        // Assert
        assertNotNull(response);
        assertTrue(response.getOrders().isEmpty()); // No orders should be returned
        assertEquals(0, response.getTotalOrders()); // Total count should be 0
    }

    @Test
    void testAdminGetAllOrdersWithInvalidPageSize() {
        // Arrange
        int page = 0;
        int size = 0;  // Invalid page size (should be >= 1)
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(30);
        LocalDateTime endDateTime = LocalDateTime.now();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.adminGetAllOrders(page, size, startDateTime, endDateTime);
        });

        assertEquals("Page size must not be less than one", exception.getMessage()); // Assert the exception message
    }






    // updateOrderStatus 테스트 코드 //

    @Test
    void testUpdateOrderStatusNotFound() {
        // Arrange
        Long orderId = 1L;
        String status = "start";

        // Mock the repository to return an empty list for order status
        when(orderToStatusMappingRepository.findByOrderIdOrderByOrderStatusCreatedAtDesc(eq(orderId), any()))
            .thenReturn(List.of()); // Return an empty list

        // Act & Assert
        NotFoundOrderException thrown = assertThrows(NotFoundOrderException.class, () -> {
            orderService.updateOrderStatus(orderId, status);
        });

        assertEquals("주문을 찾을 수 없습니다.", thrown.getMessage()); // Verify the exception message
    }



    @Test
    void testUpdateOrderStatusToStartValid() {
        // Arrange
        Long orderId = 1L;
        String status = "start";
        Customer mockCustomer = new Customer();

        // Create mock Order and Customer
        Order mockOrder = new Order(1L, mockCustomer, null
            , LocalDateTime.now(), 10000, "ORD123"
            , null, "John Doe", "010-1234-5678"
            , "12345", "Seoul", "Seoul"
            , "Special request", LocalDate.now(), null);

        // Mock OrderStatusRepository to return PENDING status (OrderStatusEnum.PENDING)
        OrderStatus mockStatus = new OrderStatus(1L, OrderStatusEnum.PENDING);
        when(orderStatusRepository.findById(2L)).thenReturn(Optional.of(mockStatus));

        // Mock the latest OrderStatus mapping with the PENDING status
        OrderToStatusMapping mockOrderStatus = new OrderToStatusMapping(mockOrder, mockStatus, LocalDateTime.now());
        when(orderToStatusMappingRepository.findByOrderIdOrderByOrderStatusCreatedAtDesc(eq(orderId), any(Pageable.class)))
            .thenReturn(List.of(mockOrderStatus));

        // Mock orderRepository to return mockOrder (Make sure it's not Optional.empty())
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));  // Return a non-empty Optional

        // Act
        boolean result = orderService.updateOrderStatus(orderId, status);  // Should succeed in changing status to 'start'

        // Assert
        assertTrue(result);
        // Add further assertions to check that the status has been updated and invoice number is generated
    }

    @Test
    void testUpdateOrderStatusToStartAlreadyShipping() {
        // Arrange
        Long orderId = 1L;
        String status = "start";

        // Mock Order and related entities
        Customer mockCustomer = new Customer();
        DeliveryRule mockDeliveryRule = new DeliveryRule();
        OrderSaveRequest request = new OrderSaveRequest(
            null, 1L, "John Buyer", "john.buyer@example.com", "1234567890",
            "Jane Doe", "9876543210", "12345", "123 Main Street",
            "Apt 101", "Leave at the door", LocalDate.now().plusDays(2),
            100, 50, 20000, 1L, "ORD12345"
        );

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

        // Mock the order repository to return the mockOrder
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        // Mock the latest OrderStatus mapping to be IN_SHOPPING (already shipped)
        OrderStatus mockStatus = new OrderStatus(2L, OrderStatusEnum.IN_SHOPPING);  // Mock the "shipping" status
        when(orderStatusRepository.findById(2L)).thenReturn(Optional.of(mockStatus));  // Mocking the repository response
        OrderToStatusMapping mockOrderStatus = new OrderToStatusMapping(mockOrder, mockStatus, LocalDateTime.now());
        when(orderToStatusMappingRepository.findByOrderIdOrderByOrderStatusCreatedAtDesc(eq(orderId), any(Pageable.class)))
            .thenReturn(List.of(mockOrderStatus));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            orderService.updateOrderStatus(orderId, status);  // Should throw exception as the order is already in shipping
        });
    }


    @Test
    void testUpdateOrderStatusToStartAlreadyCompleted() {
        // Arrange
        Long orderId = 1L;
        String status = "start";

        // Mock Order and related entities
        Customer mockCustomer = new Customer();
        DeliveryRule mockDeliveryRule = new DeliveryRule();
        OrderSaveRequest request = new OrderSaveRequest(
            null, 1L, "John Buyer", "john.buyer@example.com", "1234567890",
            "Jane Doe", "9876543210", "12345", "123 Main Street",
            "Apt 101", "Leave at the door", LocalDate.now().plusDays(2),
            100, 50, 20000, 1L, "ORD12345"
        );

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

        // Mock the order repository to return the mockOrder
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        // Mock the latest OrderStatus mapping to be COMPLETED (order is already completed)
        OrderStatus mockStatus = new OrderStatus(3L, OrderStatusEnum.COMPLETED);  // COMPLETED status
        OrderToStatusMapping mockOrderStatus = new OrderToStatusMapping(mockOrder, mockStatus, LocalDateTime.now());
        when(orderToStatusMappingRepository.findByOrderIdOrderByOrderStatusCreatedAtDesc(eq(orderId), any(Pageable.class)))
            .thenReturn(List.of(mockOrderStatus));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            orderService.updateOrderStatus(orderId, status);  // Should throw exception as the order is already completed
        });
    }

    // END UpdateOrderStatus 테스트 코드 //




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

