package com.nhnacademy.ssacthree_shop_api.orderset.refundreason.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refund_reason")
public class RefundReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성되는 ID
    @Column(name = "refund_reason_id")
    private Long id;

    @NotNull
    @Column(name = "refund_reason_name", length = 20)
    private String refundReasonName;

    @NotNull
    @Column(name = "refund_reason_is_used")
    private Boolean refundReasonIsUsed;

    @NotNull
    @Column(name = "refund_reason_created_at")
    private LocalDateTime refundReasonCreatedAt;

    @NotNull
    @Column(name = "refund_delivery_fee")
    private int refund_delivery_fee;
}
