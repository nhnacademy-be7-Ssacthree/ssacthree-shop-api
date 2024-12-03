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

    public DeliveryRule(String name, int fee, int discountCost) {
        this.deliveryRuleName = name;
        this.deliveryFee = fee;
        this.deliveryDiscountCost = discountCost;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_rule_id")
    private Long deliveryRuleId;

    @Column(name = "delivery_rule_name")
    private String deliveryRuleName;

    @Column(name = "delivery_fee")
    private int deliveryFee;

    @Column(name = "delivery_discount_cost")
    private int deliveryDiscountCost;

    @Setter
    @Column(name="delivery_rule_is_selected")
    private boolean deliveryRuleIsSelected = false;

    @Column(name = "delivery_rule_created_at")
    private LocalDateTime deliveryRuleCreatedAt = LocalDateTime.now();
}
