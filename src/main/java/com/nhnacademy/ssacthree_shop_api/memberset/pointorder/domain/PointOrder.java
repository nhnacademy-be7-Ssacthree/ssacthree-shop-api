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

    @Id
    @Column(name = "point_history_id") // Primary Key 설정
    private Long pointHistoryId;

    @OneToOne
    @MapsId // PointHistory의 Primary Key를 이 엔티티의 Primary Key로 사용
    @JoinColumn(name = "point_history_id", referencedColumnName = "point_hisory_id")
    private PointHistory pointHistory;

    @OneToOne
    @JoinColumn(name = "orders_id", nullable = false) // Foreign Key 설정
    private Order order;

    public PointOrder(PointHistory pointHistory, Order order) {
        this.pointHistory = pointHistory;
        this.order = order;
    }
}
