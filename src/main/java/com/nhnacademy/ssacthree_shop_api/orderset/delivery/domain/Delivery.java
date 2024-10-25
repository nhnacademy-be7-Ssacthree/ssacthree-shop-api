package com.nhnacademy.ssacthree_shop_api.orderset.delivery.domain;



import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "delivery_rule_id")
//    private DeliveryRule delivery_rule_id;

    @OneToOne
    @JoinColumn(name = "orders_id")
    private Order order;


}
