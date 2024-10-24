package com.nhnacademy.ssacthree_shop_api.memberset.pointorder.domain;

import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name="point_order")
public class PointOrder {

    @NotNull
    @Id
    private long id;


    @MapsId
    @OneToOne
    @JoinColumn(name="point_history_id")
    private PointHistory pointHistory;

    @OneToOne
    @JoinColumn(name="order_id")
    private Order order;
}
