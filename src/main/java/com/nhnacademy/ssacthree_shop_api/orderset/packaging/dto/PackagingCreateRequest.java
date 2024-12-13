package com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PackagingCreateRequest {

    private String name;

    private int price;

    private String imageUrl;
}
