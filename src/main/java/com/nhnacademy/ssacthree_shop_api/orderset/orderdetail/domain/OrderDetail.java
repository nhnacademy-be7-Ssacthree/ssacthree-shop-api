package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.domain;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.domain.MemberCoupon;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "order_detail")
public class OrderDetail {

    // 복합 키
    @EmbeddedId
    private OrderDetailId id;

    @ManyToOne
    @MapsId("order_id")
    @JoinColumn(name = "orders_id")
    private Order order;

    @ManyToOne
    @MapsId("book_id")
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToOne
    @JoinColumn(name = "member_coupon_id")
    private MemberCoupon memberCoupon;
    
    @NotNull
    private int quantity;

    @NotNull
    @Column(name = "bookprice_at_order")
    private int bookpriceAtOrder;


    public OrderDetail(Order order, Book book, MemberCoupon memberCoupon, int quantity, int bookpriceAtOrder) {
        this.order = order;
        this.book = book;
        this.memberCoupon = memberCoupon;
        this.quantity = quantity;
        this.bookpriceAtOrder = bookpriceAtOrder;
        // 추가해야 복합키 id 만들어짐.
        this.id = new OrderDetailId(order.getId(), book.getBookId());
    }
}
