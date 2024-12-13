package com.nhnacademy.ssacthree_shop_api.orderset.order.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.QOrder;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderListResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.impl.OrderRepositoryImpl;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.QOrderStatus;
import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.QOrderToStatusMapping;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.util.Optional;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@EntityScan(basePackages = "com.nhnacademy.ssacthree_shop_api")
class OrderRepositoryImplTest {

  @Autowired
  private TestEntityManager testEntityManager;

  @Mock
  private JPAQuery<Tuple> tupleQueryMock;
  @MockBean
  private JPAQueryFactory queryFactory;
  @Mock
  private JPAQuery<Long> countQueryMock;
  @Mock
  private OrderRepositoryImpl orderRepository;

  @InjectMocks
  private OrderRepositoryImpl orderRepositoryImpl;


  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    orderRepository = new OrderRepositoryImpl(queryFactory);
  }








  @Test
  void testFindOrderIdByOrderNumber() {
    // Given
    Customer mockCustomer = new Customer();
    DeliveryRule mockDeliveryRule = new DeliveryRule("Standard Delivery", 1000, 2000);
    testEntityManager.persist(mockCustomer);
    testEntityManager.persist(mockDeliveryRule);

    Order mockOrder = new Order(1L, mockCustomer, null, LocalDateTime.now(), 10000, "ORD123", mockDeliveryRule,
        "John Doe", "010-1234-5678", "12345", "Seoul", "Seoul", "Special request", LocalDate.now(), null);
    testEntityManager.merge(mockOrder);

    QOrder order = QOrder.order;

    // Mock JPAQuery and queryFactory
    JPAQuery<Long> queryMock = mock(JPAQuery.class);
    when(queryFactory.select(order.id)).thenReturn(queryMock);
    when(queryMock.from(order)).thenReturn(queryMock);
    when(queryMock.where(order.order_number.eq("ORD123"))).thenReturn(queryMock);
    when(queryMock.fetchFirst()).thenReturn(1L); // Example return value

    // When
    Optional<Long> result = orderRepository.findOrderIdByOrderNumber("ORD123");

    // Then
    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(mockOrder.getId());
  }


}



