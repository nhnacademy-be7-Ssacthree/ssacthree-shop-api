package com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping;

import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.domain.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_to_status_mapping")
public class OrderToStatusMapping {

    @EmbeddedId
    private OrderToStatusMappingId id;

    @MapsId("order_id")
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @MapsId("order_status_id")
    @ManyToOne
    @JoinColumn(name = "order_status_id")
    private OrderStatus orderStatus;

    @NotNull
    private LocalDateTime orderStatusCreatedAt;
}
