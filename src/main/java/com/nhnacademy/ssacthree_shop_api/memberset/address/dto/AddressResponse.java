package com.nhnacademy.ssacthree_shop_api.memberset.address.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddressResponse {

    private long addressId;
    private String addressAlias;
    private String addressRoadname;
    private String addressDetail;
    private String addressPostalNumber;
}
