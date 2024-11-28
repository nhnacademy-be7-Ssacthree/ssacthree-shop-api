package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
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
