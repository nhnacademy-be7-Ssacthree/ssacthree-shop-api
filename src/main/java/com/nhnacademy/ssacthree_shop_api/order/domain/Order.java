package com.nhnacademy.ssacthree_shop_api.order.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;


    @NotNull
    @Column(name = "customer_id")
    private Long customer_id;


    @Column(name = "member_coupon_id")
    private Long member_coupon_id;

    @NotNull
    @Column(name = "ordered_date")
    private LocalDateTime ordered_date;


    @NotNull
    @Column(name = "order_total_price")
    private int total_price;


    @NotNull
    @Column(name = "order_number")
    private String order_number;



}
