package com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain;

import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.OrderStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_status")
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_status_id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    @Column(name = "order_status_name")
    private OrderStatusEnum orderStatusEnum = OrderStatusEnum.PEENDING;
}
