package com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "delivery_rule")
public class DeliveryRule {

    public DeliveryRule(String name, int fee, int discount_cost, LocalDateTime created_at) {
        this.deliveryRuleName = name;
        this.deliveryFee = fee;
        this.deliveryDiscountCost = discount_cost;
        this.deliveryRuleCreatedAt = created_at;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_rule_id")
    private Long id;

    @Setter
    @Column(name = "delivery_rule_name")
    private String deliveryRuleName;

    @Setter
    @Column(name = "delivery_fee")
    private int deliveryFee;

    @Setter
    @Column(name = "delivery_discount_cost")
    private int deliveryDiscountCost;

    @Setter
    @Column(name="delivery_rule_is_selected")
    private boolean deliveryRuleIsSelected = false;

    @Setter
    @Column(name = "delivery_rule_created_at")
    private LocalDateTime deliveryRuleCreatedAt = LocalDateTime.now();
}
