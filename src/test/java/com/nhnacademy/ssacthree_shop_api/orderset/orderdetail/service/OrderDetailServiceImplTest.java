package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service;


import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookCommonService;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.BookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.repository.DeliveryRuleRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderDetailSaveRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepositoryCustom;
import com.nhnacademy.ssacthree_shop_api.orderset.order.service.impl.OrderServiceImpl;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.domain.OrderDetail;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.dto.OrderDetailDTO;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.dto.OrderDetailResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception.OrderNotFoundException;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.repo.OrderDetailRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service.impl.OrderDetailServiceImpl;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetailpackaging.domain.repository.OrderDetailPackagingRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.Payment;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.PaymentStatusEnum;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.PaymentType;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.repository.PaymentRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@Slf4j
class OrderDetailServiceImplTest {

  @InjectMocks
  private OrderDetailServiceImpl orderDetailService;

  @Mock
  private OrderDetailRepository orderDetailRepository;

  @Mock
  private BookCommonService bookCommonService;

  @Mock
  private BookRepository bookRepository;

  @Mock
  private OrderDetailPackagingRepository orderDetailPackagingRepository;

  @InjectMocks
  private OrderServiceImpl orderService;

  @Mock
  private OrderRepositoryCustom orderRepositoryCustom;

  @Mock
  private PaymentRepository paymentRepository;

  @Mock
  private OrderRepository orderRepository;


  @Mock
  private DeliveryRuleRepository deliveryRuleRepository;

  private Order mockOrder;
  private Book mockBook;
  private Payment mockPayment;
  private DeliveryRule mockDeliveryRule;
  private Customer mockCustomer;


  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Publisher, Category, Author, Tag 객체 생성
    Publisher publisher = new Publisher(1L, "Publisher Name", true);
    Category category = new Category(1L, "Category Name", true, null, null);
    Author author = new Author(1L, "Author Name", "Author Info");
    Tag tag = new Tag(1L, "Tag Name");
    mockDeliveryRule = new DeliveryRule("DeliveryRule Name", 1000, 100); // 예시로 배송비 1000을 설정
    mockCustomer = new Customer("John Doe", "john.doe@example.com", "010-1234-5678");

    // Book 객체 생성
    mockBook = new Book(
        1L,
        "Book1",
        "Index",
        "Book Description",
        "1234567890",
        LocalDateTime.now(),
        2500,
        2000,
        false,
        10,
        "thumbnail.jpg",
        100,
        20,
        BookStatus.ON_SALE,
        publisher,
        new HashSet<>(), // Set<BookCategory> 초기화
        new HashSet<>(), // Set<BookAuthor> 초기화
        new HashSet<>()  // Set<BookTag> 초기화
    );

    // BookCategory, BookAuthor, BookTag 객체 생성
    BookCategory bookCategory = new BookCategory(mockBook, category);
    BookAuthor bookAuthor = new BookAuthor(1L, author, mockBook);
    BookTag bookTag = new BookTag(mockBook, tag);

    // Set에 해당 객체들 추가
    mockBook.getBookCategories().add(bookCategory);
    mockBook.getBookAuthors().add(bookAuthor);
    mockBook.getBookTags().add(bookTag);

    // Order 객체 생성
    mockOrder = new Order(
        1L,
        mockCustomer,
        null,
        LocalDateTime.now(),
        10000,
        "ORD12345",
        mockDeliveryRule,
        "John Doe",
        "010-1234-5678",
        "12345",
        "Road Address",
        "Detail Address",
        "No request",
        null,
        "Invoice123"
    );

    // Initialize Payment
    mockPayment = new Payment(
        1L,
        mockOrder,
        new PaymentType(1L, "Credit Card", true, LocalDateTime.now()),
        LocalDateTime.now(),
        33000,
        "PAY12345",
        PaymentStatusEnum.DONE
    );


    when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder)); // orderRepository Mock 설정
    when(paymentRepository.findByOrder(mockOrder)).thenReturn(Optional.of(mockPayment)); // paymentRepository Mock 설정
    when(deliveryRuleRepository.findById(mockOrder.getDeliveryRuleId().getDeliveryRuleId()))
        .thenReturn(Optional.of(mockDeliveryRule));

    // Mock 설정
    when(bookRepository.findByBookId(1L)).thenReturn(Optional.of(mockBook)); // bookRepository Mock 설정
  }







  // ----------- saveOrderDetails Test ----------- //
  @Test
  void saveOrderDetailsTest() {
    // Given
    OrderDetailSaveRequest request = new OrderDetailSaveRequest(
        1L, 1L, null, 2, 2000, null
    );

    BookInfoResponse bookInfo = BookInfoResponse.builder()
        .bookId(1L)
        .bookName("Book1")
        .bookIndex("Index")
        .bookInfo("Book Description")
        .bookIsbn("123456789")
        .publicationDate(LocalDateTime.now())
        .regularPrice(2500)
        .salePrice(2000)
        .isPacked(false)
        .stock(10)
        .bookThumbnailImageUrl("thumbnail.jpg")
        .bookViewCount(100)
        .bookDiscount(20)
        .bookStatus("ON_SALE")
        .publisher(new PublisherNameResponse(1L, "Publisher Name"))
        .categories(List.of(new CategoryNameResponse(1L, "Category")))
        .tags(List.of(new TagInfoResponse(1L, "Tag")))
        .authors(List.of(new AuthorNameResponse(1L, "Author")))
        .build();

    when(bookCommonService.getBook(1L)).thenReturn(bookInfo);

    // When
    orderDetailService.saveOrderDetails(mockOrder, List.of(request));

    // Then
    verify(bookCommonService, times(1)).getBook(request.getBookId());
    verify(bookRepository, times(1)).findByBookId(request.getBookId());
    verify(orderDetailRepository, times(1)).saveAll(anyList());
    verify(orderDetailPackagingRepository, times(1)).saveAll(anyList());
  }






  // ----------- getOrderId Test ----------- //
  @Test
  void getOrderId_Success() {
    // Given
    String orderNumber = "ORD12345";
    Long expectedOrderId = 1L;

    when(orderRepositoryCustom.findOrderIdByOrderNumber(orderNumber)).thenReturn(Optional.of(expectedOrderId));

    // When
    Optional<Long> actualOrderId = orderRepositoryCustom.findOrderIdByOrderNumber(orderNumber);

    // Then
    assertTrue(actualOrderId.isPresent());
    assertEquals(expectedOrderId, actualOrderId.get());
    verify(orderRepositoryCustom, times(1)).findOrderIdByOrderNumber(orderNumber);
  }

  @Test
  void getOrderId_OrderNotFound() {
    // Given
    String orderNumber = "NON_EXISTENT";

    when(orderRepositoryCustom.findOrderIdByOrderNumber(orderNumber)).thenReturn(Optional.empty());

    // When
    Optional<Long> actualOrderId = orderRepositoryCustom.findOrderIdByOrderNumber(orderNumber);

    // Then
    assertFalse(actualOrderId.isPresent());
    verify(orderRepositoryCustom, times(1)).findOrderIdByOrderNumber(orderNumber);
  }





  // ----------- ComparePhoneNumber Test ----------- //

  @Test
  void testComparePhoneNumber_Success() {
    // Given
    Long orderId = 1L;
    String phoneNumber = "010-1234-5678";  // Valid phone number (matches the customer's phone number)

    // When
    Boolean result = orderDetailService.comparePhoneNumber(orderId, phoneNumber);

    // Then
    assertTrue(result);  // Phone numbers should match, so the result should be true
  }

  @Test
  void testComparePhoneNumber_Failure() {
    // Given
    Long orderId = 1L;
    String phoneNumber = "010-9876-5432";  // Invalid phone number (does not match the customer's phone number)

    // When
    Boolean result = orderDetailService.comparePhoneNumber(orderId, phoneNumber);

    // Then
    assertFalse(result);  // Phone numbers do not match, so the result should be false
  }

  @Test
  void testComparePhoneNumber_OrderNotFound() {
    // Given
    Long orderId = 999L;  // Non-existent order ID
    String phoneNumber = "010-1234-5678";  // Any phone number

    // Mock: When orderRepository.findById is called, return empty (i.e., order not found)
    when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(OrderNotFoundException.class, () -> {
      orderDetailService.comparePhoneNumber(orderId, phoneNumber);  // Expecting an exception to be thrown
    });
  }






  //----------- getOrderDetail 테스트 -----------//
  @Test
  void getOrderDetail_Success() {
    // Given
    Long orderId = 1L;

    // Mock OrderDetailRepository to return order details associated with the orderId
    OrderDetail mockOrderDetail = new OrderDetail(
        mockOrder,
        mockBook,
        null,
        2, // Quantity
        2000 // Book price at the time of order
    );

    when(orderDetailRepository.findByOrderId(orderId)).thenReturn(List.of(mockOrderDetail));

    // Mock PaymentRepository to return a payment object
    when(paymentRepository.findByOrder(mockOrder)).thenReturn(Optional.of(mockPayment));

    // Mock DeliveryRule to return a delivery fee
    when(deliveryRuleRepository.findById(mockOrder.getDeliveryRuleId().getDeliveryRuleId())).thenReturn(Optional.of(mockDeliveryRule));

    // When
    OrderDetailResponse orderDetailResponse = orderDetailService.getOrderDetail(orderId);

    // Then
    // Verify if the order details have been mapped correctly
    assertNotNull(orderDetailResponse);
    assertEquals(orderId, orderDetailResponse.getOrderId());
    assertEquals(mockOrder.getOrder_number (), orderDetailResponse.getOrderNumber());
    assertEquals(mockPayment.getId(), orderDetailResponse.getPaymentId());
    assertEquals(mockPayment.getPaymentAmount(), orderDetailResponse.getPaymentAmount());
    assertEquals(mockPayment.getPaymentKey(), orderDetailResponse.getPaymentKey());
    assertEquals("결제 완료", orderDetailResponse.getPaymentStatus());
    assertEquals(mockPayment.getPaymentType().getPaymentTypeName(), orderDetailResponse.getPaymentTypeName());
    assertEquals(mockOrder.getTotal_price(), orderDetailResponse.getOrderTotalPrice());

    // Verify if the order detail information is mapped correctly
    assertEquals(1, orderDetailResponse.getOrderDetailList().size());
    OrderDetailDTO orderDetailDTO = orderDetailResponse.getOrderDetailList().get(0);
    assertEquals(mockBook.getBookId(), orderDetailDTO.getBookId());
    assertEquals(mockBook.getBookName(), orderDetailDTO.getBookName());
    assertEquals(mockBook.getBookThumbnailImageUrl(), orderDetailDTO.getBookThumbnailImageUrl());
    assertEquals(2, orderDetailDTO.getQuantity());
    assertEquals(2000, orderDetailDTO.getBookPriceAtOrder());

    // Verify interactions with repositories
    verify(orderDetailRepository, times(1)).findByOrderId(orderId);
    verify(paymentRepository, times(1)).findByOrder(mockOrder);
  }



//----------- convertPaymentStatus 테스트 ----------- //

  @Test
  void testConvertPaymentStatus_Done() {
    // Given
//    when(mockPayment.getPaymentStatus()).thenReturn(PaymentStatusEnum.DONE);  // PaymentStatus가 DONE으로 설정

    // When
    String result = orderDetailService.convertPaymentStatus(mockPayment);  // 상태 변환 메서드 호출

    // Then
    assertEquals("결제 완료", result);  // "결제 완료"가 반환되는지 검증
  }

  @Test
  void testConvertPaymentStatus_Cancelled() {
    // Given
    Payment     mockPayment2 = new Payment(
        1L,
        mockOrder,
        new PaymentType(1L, "Credit Card", true, LocalDateTime.now()),
        LocalDateTime.now(),
        33000,
        "PAY12345",
        PaymentStatusEnum.CANCEL
    );

    // When
    String result = orderDetailService.convertPaymentStatus(mockPayment2);  // 상태 변환 메서드 호출

    // Then
    assertEquals("결제 취소", result);  // "결제 취소"가 반환되는지 검증
  }
}
