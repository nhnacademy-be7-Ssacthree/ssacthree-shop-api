package com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackagingCreateRequest {

    private String name;

    private int price;

    private String imageUrl;
}
