package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.domain.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderDetailId implements Serializable {

    @Column(name = "orders_id")  // 이 부분은 그대로 두어야 합니다.
    private Long order_id;  // Order의 id와 매핑됨

    @Column(name = "book_id")
    private Long book_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetailId)) return false;
        OrderDetailId that = (OrderDetailId) o;
        return Objects.equals(order_id, that.order_id) &&
                Objects.equals(book_id, that.book_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order_id, book_id);
    }
}
