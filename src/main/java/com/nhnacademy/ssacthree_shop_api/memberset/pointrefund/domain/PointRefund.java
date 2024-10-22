package com.nhnacademy.ssacthree_shop_api.memberset.pointrefund.domain;

import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import com.nhnacademy.ssacthree_shop_api.order.domain.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name="point_refund")
public class PointRefund {

    @NotNull
    @Id
    private long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="point_history_id")
    private PointHistory pointHistory;


    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;
}
