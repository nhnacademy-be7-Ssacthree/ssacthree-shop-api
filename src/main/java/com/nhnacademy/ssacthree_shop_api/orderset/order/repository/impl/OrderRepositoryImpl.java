package com.nhnacademy.ssacthree_shop_api.orderset.order.repository.impl;

import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.QOrder;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.AdminOrderListResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderListResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepositoryCustom;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.QOrderStatus;
import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.QOrderToStatusMapping;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Slf4j
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderListResponse> findOrdersByCustomerAndDate(
            Long customerId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {
        QOrder order = QOrder.order;
        QOrderToStatusMapping orderToStatusMapping = QOrderToStatusMapping.orderToStatusMapping;
        QOrderStatus orderStatus = QOrderStatus.orderStatus;

      List<Tuple> rawResults = queryFactory
          .select(
              order.id,
              order.ordered_date,
              order.total_price,
              orderToStatusMapping.orderStatus.orderStatusEnum.stringValue()
          )
          .from(order)
          .leftJoin(orderToStatusMapping).on(
              order.id.eq(orderToStatusMapping.order.id)
                  .and(orderToStatusMapping.orderStatusCreatedAt.eq(
                      JPAExpressions.select(orderToStatusMapping.orderStatusCreatedAt.max())
                          .from(orderToStatusMapping)
                          .where(orderToStatusMapping.order.id.eq(order.id))
                  ))
          )
          .leftJoin(orderStatus).on(orderToStatusMapping.orderStatus.id.eq(orderStatus.id))
          .where(
              order.customer.customerId.eq(customerId)
                  .and(order.ordered_date.between(startDate, endDate))
          )
          .offset(pageable.getOffset())
          .limit(pageable.getPageSize())
          .fetch();

        List<OrderListResponse> results = rawResults.stream()
                .map(tuple -> new OrderListResponse(
                        tuple.get(order.id),
                        Objects.requireNonNull(tuple.get(order.ordered_date)).toLocalDate(), // LocalDateTime → LocalDate 변환
                        tuple.get(order.total_price),
                        tuple.get(orderToStatusMapping.orderStatus.orderStatusEnum.stringValue())
                ))
                .collect(Collectors.toList());

        Long total = queryFactory
                .select(order.count())
                .from(order)
                .where(
                        order.customer.customerId.eq(customerId)
                                .and(order.ordered_date.between(startDate, endDate))
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }


    @Override
    public Page<AdminOrderListResponse> adminFindAllOrders(LocalDateTime startDate,
                                                           LocalDateTime endDate,
                                                           Pageable pageable) {
        QOrder order = QOrder.order;
        QOrderToStatusMapping orderToStatusMapping = new QOrderToStatusMapping("subOrderToStatusMapping");
        QOrderStatus orderStatus = QOrderStatus.orderStatus;

        List<Tuple> rawResults = queryFactory
                .select(
                        order.id,
                        order.ordered_date,
                        order.total_price,
                        orderToStatusMapping.orderStatus.orderStatusEnum.stringValue(),
                        orderToStatusMapping.orderStatusCreatedAt,
                        order.customer.customerName,
                        order.order_number,
                        order.invoice_number
                )
                .from(order)
                .leftJoin(orderToStatusMapping)
                .on(order.id.eq(orderToStatusMapping.order.id)
                        .and(orderToStatusMapping.orderStatusCreatedAt.eq(
                                JPAExpressions.select(orderToStatusMapping.orderStatusCreatedAt.max())
                                        .from(orderToStatusMapping)
                                        .where(orderToStatusMapping.order.id.eq(order.id))
                        ))
                )
                .leftJoin(orderStatus).on(orderToStatusMapping.orderStatus.id.eq(orderStatus.id))
                .where(
                        order.ordered_date.between(startDate, endDate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<AdminOrderListResponse> results = rawResults.stream()
                .map(tuple -> new AdminOrderListResponse(
                        tuple.get(order.id),
                        Objects.requireNonNull(tuple.get(order.ordered_date)).toLocalDate(), // LocalDateTime → LocalDate 변환
                        tuple.get(order.total_price),
                        tuple.get(orderToStatusMapping.orderStatus.orderStatusEnum.stringValue()),
                        Objects.requireNonNull(tuple.get(orderToStatusMapping.orderStatusCreatedAt)).toLocalDate(),
                        tuple.get(order.customer.customerName),
                        tuple.get(order.order_number),
                        tuple.get(order.invoice_number)
                ))
                .collect(Collectors.toList());

        Long total = queryFactory
                .select(order.count())
                .from(order)
                .where(
                        order.ordered_date.between(startDate, endDate)
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }


  @Override
  public Optional<Long> findOrderIdByOrderNumber(String orderNumber) {
    QOrder order = QOrder.order;

    Long orderId = queryFactory
        .select(order.id)
        .from(order)
        .where(order.order_number.eq(orderNumber))
        .fetchFirst();  // 중복 데이터 발생 시 하나만 가져오게 합니다. (NonUniqueResultException 발생 방지)

    return Optional.ofNullable(orderId);
  }
}
