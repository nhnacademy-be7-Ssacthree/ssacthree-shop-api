package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeOrderStatusRequest {
    private Long orderId;
    private String status;

}