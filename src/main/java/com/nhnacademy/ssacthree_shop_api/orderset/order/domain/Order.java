package com.nhnacademy.ssacthree_shop_api.orderset.order.domain;

import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.domain.MemberCoupon;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
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
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @OneToOne
    @JoinColumn(name = "member_coupon_id")
    private MemberCoupon memberCoupon;

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
