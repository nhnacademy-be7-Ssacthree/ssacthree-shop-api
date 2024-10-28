package com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderToStatusMappingId {
    private Long order_id;
    private Long order_status_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderToStatusMappingId)) return false;
        OrderToStatusMappingId that = (OrderToStatusMappingId) o;
        return Objects.equals(order_id, that.order_id) &&
                Objects.equals(order_status_id, that.order_status_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order_id, order_status_id);
    }
}
