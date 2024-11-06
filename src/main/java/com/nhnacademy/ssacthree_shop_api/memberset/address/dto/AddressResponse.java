package com.nhnacademy.ssacthree_shop_api.memberset.address.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddressResponse {
    private String addressAlias;
    private String addressDetail;
    private String addressRoadname;
    private String addressPostalNumber;
}
