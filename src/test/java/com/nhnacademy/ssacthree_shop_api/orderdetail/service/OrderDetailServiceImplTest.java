//package com.nhnacademy.ssacthree_shop_api.orderdetail.service;
//
//
//import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
//import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
//import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
//import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
//import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookCommonService;
//import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
//import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.BookCategory;
//import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTag;
//import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
//import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
//import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
//import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
//import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderDetailSaveRequest;
//import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepository;
//import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.dto.OrderDetailResponse;
//import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception.OrderNotFoundException;
//import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception.PaymentNotFoundException;
//import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.repo.OrderDetailRepository;
//import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service.impl.OrderDetailServiceImpl;
//import com.nhnacademy.ssacthree_shop_api.orderset.orderdetailpackaging.domain.repository.OrderDetailPackagingRepository;
//import com.nhnacademy.ssacthree_shop_api.orderset.packaging.domain.Packaging;
//import com.nhnacademy.ssacthree_shop_api.orderset.packaging.repository.PackagingRepository;
//import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.Payment;
//import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.PaymentStatusEnum;
//import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.PaymentType;
//import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.repository.PaymentRepository;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.util.ArrayList;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//class OrderDetailServiceImplTest {
//
//  @Mock
//  private OrderRepository orderRepository;
//
//  @Mock
//  private OrderDetailRepository orderDetailRepository;
//
//  @Mock
//  private BookRepository bookRepository;
//
//  @Mock
//  private PaymentRepository paymentRepository;
//
//  @InjectMocks
//  private OrderDetailServiceImpl orderDetailServiceImpl;
//
//  @Mock
//  private OrderDetailPackagingRepository orderDetailPackagingRepository;
//
//  @Mock
//  private BookCommonService bookCommonService;
//
//  @Mock
//  private PackagingRepository packagingRepository;
//
//  private List<OrderDetailSaveRequest> orderDetailSaveRequestList;
//
//  private Order order;
//  private Book book;
//  private OrderDetailSaveRequest orderDetailSaveRequest;
//
//  @BeforeEach
//  void setUp() {
//    MockitoAnnotations.openMocks(this);
//
//    // Create mock objects for the test
//    order = new Order(); // Initialize order with necessary fields.
//    book = new Book(); // Initialize book with necessary fields.
//    orderDetailSaveRequest = new OrderDetailSaveRequest(1L, 1L, null, 2, 1000, 1L);
//    DeliveryRule deliveryRule = new DeliveryRule("Standard Delivery", 500, 100);
//    Customer customer = new Customer(1L, "John Doe", "010-1234-5678", "john.doe@example.com");
//
//
//    order = new Order(
//        1L, // id
//        customer, // customer 객체 (미리 생성된 객체)
//        null, // memberCoupon 객체 (미리 생성된 객체)
//        LocalDateTime.now(), // ordered_date
//        2000, // total_price
//        "ORD123", // order_number
//        deliveryRule, // deliveryRuleId 객체 (미리 생성된 객체)
//        "John Doe", // receiverName
//        "010-1234-5678", // receiverPhone
//        "12345", // postalCode
//        "123 Main St", // roadAddress
//        "Apt 101", // detailAddress
//        "Please deliver quickly.", // orderRequest
//        LocalDate.now().plusDays(5), // deliveryDate
//        null // invoice_number (옵션)
//    );
//
//
//    PaymentType paymentType = new PaymentType();
//
//// In the setup code for the test:
//    Payment payment = new Payment(
//        1L,
//        order, // Order object
//        paymentType, // PaymentType object
//        LocalDateTime.now(), // Current time
//        1000, // Payment amount
//        "payment-key-123", // Payment key
//        PaymentStatusEnum.DONE // Payment status
//    );
//
//    // Mock repository methods
//    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
//    when(bookRepository.findByBookId(1L)).thenReturn(Optional.of(book));
//    when(paymentRepository.findByOrder(order)).thenReturn(Optional.of(payment));
//
//
//    // Mocking book repository
//    when(bookRepository.findByBookId(1L)).thenReturn(Optional.of(book));
//
//    // Mocking payment repository
//    when(bookCommonService.getBook(anyLong())).thenReturn(new BookInfoResponse(book)); // Mock book info service
//
//    // Mocking packaging repository
//    Packaging packaging = new Packaging("Standard Packaging", 500, "http://image.url/packaging.jpg");
//    when(packagingRepository.findById(1L)).thenReturn(Optional.of(packaging));
//  }
//
//
//
//  @Test
//  void comparePhoneNumber_shouldReturnTrueWhenPhoneNumbersMatch() {
//    // Given
//    String phoneNumber = "010-1234-5678";
//    order.getCustomer().setCustomerPhoneNumber(phoneNumber);
//
//    // When
//    Boolean result = orderDetailServiceImpl.comparePhoneNumber(order.getId(), phoneNumber);
//
//    // Then
//    assertTrue(result);
//  }
//
//  @Test
//  void comparePhoneNumber_shouldReturnFalseWhenPhoneNumbersDoNotMatch() {
//    // Given
//    String phoneNumber = "010-1234-5678";
//    order.getCustomer().setCustomerPhoneNumber("010-8765-4321");
//
//    // When
//    Boolean result = orderDetailServiceImpl.comparePhoneNumber(order.getId(), phoneNumber);
//
//    // Then
//    assertFalse(result);
//  }
//
//  @Test
//  void getOrderDetail_shouldReturnOrderDetailResponse() {
//    // Given: Initialize necessary objects
//
//    // Initialize order with fields
//    DeliveryRule deliveryRule = new DeliveryRule("Standard Delivery", 500, 100);
//    Customer customer = new Customer(1L, "John Doe", "010-1234-5678", "john.doe@example.com");
//    Order order = new Order(
//        1L, // id
//        customer, // customer object
//        null, // memberCoupon (null as per your requirement)
//        LocalDateTime.now(), // ordered_date
//        2000, // total_price
//        "ORD123", // order_number
//        deliveryRule, // deliveryRule object
//        "John Doe", // receiverName
//        "010-1234-5678", // receiverPhone
//        "12345", // postalCode
//        "123 Main St", // roadAddress
//        "Apt 101", // detailAddress
//        "Please deliver quickly.", // orderRequest
//        LocalDate.now().plusDays(5), // deliveryDate
//        null // invoice_number
//    );
//
//    // Initialize payment
//    PaymentType paymentType = new PaymentType();
//    Payment payment = new Payment(
//        1L, // payment ID
//        order, // associated order
//        paymentType, // payment type
//        LocalDateTime.now(), // payment time
//        1000, // payment amount
//        "payment-key-123", // payment key
//        PaymentStatusEnum.DONE // payment status
//    );
//
//    // Mock the repository methods
//    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
//    when(orderDetailRepository.findByOrderId(1L)).thenReturn(new ArrayList<>()); // Mock empty order details
//    when(paymentRepository.findByOrder(order)).thenReturn(Optional.of(payment)); // Mock payment for the order
//
//    // When: Call the service method
//    OrderDetailResponse response = orderDetailServiceImpl.getOrderDetail(1L);
//
//    // Then: Verify the results
//    assertNotNull(response);
//    assertEquals(order.getId(), response.getOrderId());
//    assertEquals(payment.getId(), response.getPaymentId());
//    assertEquals(payment.getPaymentAmount(), response.getPaymentAmount());
//    assertEquals(payment.getPaymentKey(), response.getPaymentKey());
//    assertEquals("결제 완료", response.getPaymentStatus()); // Assuming your method converts status correctly
//  }
//
//
//  @Test
//  void getOrderDetail_shouldThrowOrderNotFoundExceptionWhenOrderNotFound() {
//    // Given
//    when(orderRepository.findById(1L)).thenReturn(Optional.empty());
//
//    // When / Then
//    assertThrows(OrderNotFoundException.class, () -> {
//      orderDetailServiceImpl.getOrderDetail(1L);
//    });
//  }
//
//  @Test
//  void getOrderDetail_shouldThrowPaymentNotFoundExceptionWhenPaymentNotFound() {
//    // Given
//    when(paymentRepository.findByOrder(order)).thenReturn(Optional.empty());
//
//    // When / Then
//    assertThrows(PaymentNotFoundException.class, () -> {
//      orderDetailServiceImpl.getOrderDetail(1L);
//    });
//  }
//
//
//
//  @Test
//  void testSaveOrderDetails() {
////    // 책 정보 생성
//    Set<BookCategory> bookCategories = new HashSet<>();
//    Set<BookAuthor> bookAuthors = new HashSet<>();
//    Set<BookTag> bookTags = new HashSet<>();
//
//
//    Book book = new Book(1L, "Test Book", "Test Index", "Test Info", "123-456-789",
//        LocalDateTime.now(), 1000, 800, true, 10, "image-url", 0, 10,
//        BookStatus.ON_SALE, new Publisher(1L, "Test Publisher", true),
//        bookCategories, bookAuthors, bookTags);
//
//
//
//    // Mocking dependencies
//    when(bookRepository.findByBookId(1L)).thenReturn(Optional.of(book));  // 책 정보 반환
//    when(orderDetailRepository.saveAll(anyList())).thenReturn(new ArrayList<>());  // 주문 세부 정보 저장
//    when(orderDetailPackagingRepository.saveAll(anyList())).thenReturn(new ArrayList<>());  // 포장 정보 저장
//
//    // 재고 확인 (초기값 10개)
//    assertEquals(10, book.getStock());
//
//    // saveOrderDetails 호출
//    orderDetailServiceImpl.saveOrderDetails(order, List.of(orderDetailSaveRequest));
//
//    // 책 재고가 2개 차감되었는지 확인 (10 - 2 = 8)
//    assertEquals(8, book.getStock());
//
//    // BookRepository에서 findByBookId가 1번 호출되었는지 확인
//    verify(bookRepository, times(1)).findByBookId(1L);
//
//    // OrderDetailRepository에서 saveAll이 1번 호출되었는지 확인
//    verify(orderDetailRepository, times(1)).saveAll(anyList());
//
//    // OrderDetailPackagingRepository에서 saveAll이 1번 호출되었는지 확인
//    verify(orderDetailPackagingRepository, times(1)).saveAll(anyList());
//  }
//
//  @Test
//  void testSaveOrderDetails_InsufficientStock() {
//
//  }
//
//
//
//}
