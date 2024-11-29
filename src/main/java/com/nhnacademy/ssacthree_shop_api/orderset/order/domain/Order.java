package com.nhnacademy.ssacthree_shop_api.orderset.order.domain;

import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.domain.MemberCoupon;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "orders_id")
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

    // 바뀐 entity
    @NotNull
    @ManyToOne
    @JoinColumn(name = "delivery_rule_id")
    private DeliveryRule deliveryRuleId;

    @NotNull
    @Column(name = "receiver_name")
    private String receiverName;

    @NotNull
    @Column(name = "receiver_phone_number")
    private String receiverPhone;

    @NotNull
    @Column(name = "delivery_postal_number")
    private String postalCode;

    @NotNull
    @Column(name = "delivery_address_roadname")
    private String roadAddress;

    @NotNull
    @Column(name = "delivery_address_detail")
    private String detailAddress;

    @NotNull
    @Column(name = "order_request")
    private String orderRequest;

    @NotNull
    @Column(name = "delivery_set_date")
    private LocalDate deliveryDate;

    @Setter
    @Column(name = "order_invoice_number")
    private String invoice_number;
}
