package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminOrderListResponse {
    private Long orderId;
    private LocalDate orderDate;
    private int totalPrice;
    private String orderStatus;
    private LocalDate orderStatusCreatedAt;
    private String customerName;
    private String orderNumber;
    private String invoiceNumber;

}