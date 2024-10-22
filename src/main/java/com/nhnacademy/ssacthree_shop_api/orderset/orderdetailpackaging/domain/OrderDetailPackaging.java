package com.nhnacademy.ssacthree_shop_api.orderset.orderdetailpackaging.domain;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.domain.Packaging;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderDetailPackaging {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_packaging_id")
    private Long orderDetailPackagingId;


    @NotNull
    @ManyToOne
    @JoinColumn(name = "packaging_id")
    private Packaging packaging;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;


    @NotNull
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @NotNull
    private int packagingQuantity;
}
