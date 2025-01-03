package com.nhnacademy.ssacthree_shop_api.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CustomerCreateRequest {

    private String customerName;
    private String customerPhoneNumber;
    private String customerEmail;

}
