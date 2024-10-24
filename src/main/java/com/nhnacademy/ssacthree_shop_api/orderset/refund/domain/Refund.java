package com.nhnacademy.ssacthree_shop_api.orderset.refund.domain;

import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.domain.domain.OrderDetail;
import com.nhnacademy.ssacthree_shop_api.orderset.refundreason.domain.RefundReason;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refund")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_id")
    private Long id;

    @NotNull
    @Column(name = "refund_quantity")
    private int refundQuantity;

    @NotNull
    @Column(name = "refund_price")
    private int refundPrice;

    @NotNull
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "order_id", referencedColumnName = "order_id"),
            @JoinColumn(name = "book_id", referencedColumnName = "book_id")
    })
    private OrderDetail orderDetail; // order_detail 참조

    @NotNull
    @ManyToOne
    @JoinColumn(name = "refund_reason_id")
    private RefundReason refundReason;
}
