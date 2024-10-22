package com.nhnacademy.ssacthree_shop_api.orderset.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class PaymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_type_id")
    private Long id;

    @NotNull
    private String paymentTypeName;

    @NotNull
    private Boolean paymentTypeIsUsed;

    @NotNull
    private LocalDateTime paymentTypeCreatedAt;
}
