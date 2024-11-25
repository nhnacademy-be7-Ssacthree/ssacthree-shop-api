package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderDetailSaveRequest {

    private Long bookId;
    private Long orderId;
    private Long couponId;
    private Integer quantity;
    private Integer bookPriceAtOrder;
}
