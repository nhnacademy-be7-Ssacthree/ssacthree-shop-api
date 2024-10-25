package com.nhnacademy.ssacthree_shop_api.customer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Customer {

    public Customer(String customerName, String customerEmail, String customerPhoneNumber) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhoneNumber = customerPhoneNumber;
    }


    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long customerId;

    @Setter
    private String customerName;

    @Setter
    private String customerEmail;

    @Setter
    private String customerPhoneNumber;


}
