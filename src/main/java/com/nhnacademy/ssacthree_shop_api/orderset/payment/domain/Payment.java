package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain;

import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성되는 ID
    @Column(name = "payment_id")
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    private Order order; // 주문 ID와 연결

    @NotNull
    @ManyToOne
    @JoinColumn(name = "payment_type_id", referencedColumnName = "payment_type_id", nullable = false)
    private PaymentType paymentType;

    @NotNull
    @Column(name = "payment_created_at")
    private LocalDateTime paymentCreatedAt;

    @NotNull
    @Column(name = "payment_amount")
    private int paymentAmount;

    @Column(name = "payment_key")
    private String paymentKey;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum paymentStatus;
}
