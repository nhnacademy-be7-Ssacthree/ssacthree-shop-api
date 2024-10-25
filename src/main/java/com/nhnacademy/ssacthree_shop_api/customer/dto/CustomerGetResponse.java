package com.nhnacademy.ssacthree_shop_api.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomerGetResponse {

    private long customerId;
    private String customerName;
    private String customerPhoneNumber;
    private String customerEmail;
}
