package com.nhnacademy.ssacthree_shop_api.order.orderdetail.domain;

<<<<<<< Updated upstream:src/main/java/com/nhnacademy/ssacthree_shop_api/order/domain/OrderDetail.java
=======
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.domain.MemberCoupon;
import com.nhnacademy.ssacthree_shop_api.order.domain.Order;
>>>>>>> Stashed changes:src/main/java/com/nhnacademy/ssacthree_shop_api/order/orderdetail/domain/OrderDetail.java
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail {

    // 복합 키
    @EmbeddedId
    private OrderDetailId id;

    @ManyToOne
    @MapsId("order_id")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("book_id")
    @JoinColumn(name = "book_id")
    private Book book;

<<<<<<< Updated upstream:src/main/java/com/nhnacademy/ssacthree_shop_api/order/domain/OrderDetail.java
    @Column(name = "member_coupon_id")
    private Long memberCouponId;
=======
    @OneToOne
    @JoinColumn(name = "member_coupon_id")
    private MemberCoupon memberCoupon;
>>>>>>> Stashed changes:src/main/java/com/nhnacademy/ssacthree_shop_api/order/orderdetail/domain/OrderDetail.java

    
    @NotNull
    private int quantity;

    @NotNull
    @Column(name = "bookprice_at_order")
    private int bookpriceAtOrder;
}
