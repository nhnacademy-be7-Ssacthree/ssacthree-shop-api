package com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping;

import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "order_to_status_mapping")
public class OrderToStatusMapping {

    @EmbeddedId
    private OrderToStatusMappingId id;

    @MapsId("order_id")
    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Order order;

    @MapsId("order_status_id")
    @ManyToOne
    @JoinColumn(name = "order_status_id")
    private OrderStatus orderStatus;

    @NotNull
    @Column(name = "order_status_created_at")
    private LocalDateTime orderStatusCreatedAt;

    public OrderToStatusMapping(Order order, OrderStatus orderStatus, LocalDateTime orderStatusCreatedAt) {
        this.order = order;
        this.orderStatus = orderStatus;
        this.orderStatusCreatedAt = orderStatusCreatedAt;
        this.id = new OrderToStatusMappingId(order.getId(), orderStatus.getId());
    }
}
