package com.nhnacademy.ssacthree_shop_api.order.domain;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderToStatusMappingId {
    private Long order_id;
    private Long order_status_id;
}
