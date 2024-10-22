package com.nhnacademy.ssacthree_shop_api.customer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Customer {

    @NotNull
    @Id
    @Column(name="customer_id")
    private long customerId;

    @NotNull
    @Size(min = 1, max = 20)
    private String customerName;

    @NotNull
    @Size(min = 13, max = 13)
    private String customerEmail;

    @NotNull
    @Size(min = 1, max = 50)
    private String customerPhoneNumber;


}
