package com.nhnacademy.ssacthree_shop_api.order.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail {

    // 복합 키
    @EmbeddedId
    private OrderDetailId id;

    @ManyToOne
    @MapsId("order_id")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("book_id")
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "member_coupon_id")
    private Long memberCouponId;

    @NotNull
    private int quantity;

    @NotNull
    @Column(name = "bookprice_at_order")
    private int bookpriceAtOrder;
}
