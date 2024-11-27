package com.nhnacademy.ssacthree_shop_api.orderset.order.repository.impl;

import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.QOrder;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.AdminOrderListResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderListResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepositoryCustom;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.QOrderStatus;
import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.QOrderToStatusMapping;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
        .leftJoin(orderToStatusMapping).on(order.id.eq(orderToStatusMapping.order.id))
        .leftJoin(orderStatus).on(orderToStatusMapping.orderStatus.id.eq(orderStatus.id))
        .where(
            order.customer.customerId.eq(customerId)
                .and(order.ordered_date.between(startDate, endDate))
        )
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
        QOrderToStatusMapping orderToStatusMapping = QOrderToStatusMapping.orderToStatusMapping;
        QOrderStatus orderStatus = QOrderStatus.orderStatus;

        List<Tuple> rawResults = queryFactory
                .select(
                        order.id,
                        order.ordered_date,
                        order.total_price,
                        orderToStatusMapping.orderStatus.orderStatusEnum.stringValue(),
                        order.customer.customerName,
                        order.order_number
                )
                .from(order)
                .leftJoin(orderToStatusMapping).on(order.id.eq(orderToStatusMapping.order.id))
                .leftJoin(orderStatus).on(orderToStatusMapping.orderStatus.id.eq(orderStatus.id))
                .where(
                        order.ordered_date.between(startDate, endDate)
                )
                .fetch();

        List<AdminOrderListResponse> results = rawResults.stream()
                .map(tuple -> new AdminOrderListResponse(
                        tuple.get(order.id),
                        Objects.requireNonNull(tuple.get(order.ordered_date)).toLocalDate(), // LocalDateTime → LocalDate 변환
                        tuple.get(order.total_price),
                        tuple.get(orderToStatusMapping.orderStatus.orderStatusEnum.stringValue()),
                        tuple.get(order.customer.customerName),
                        tuple.get(order.order_number)
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

}
