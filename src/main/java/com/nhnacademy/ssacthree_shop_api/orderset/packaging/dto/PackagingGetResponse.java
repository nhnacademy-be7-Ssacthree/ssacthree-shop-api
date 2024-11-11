package com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PackagingGetResponse {
    private Long id;
    private String packagingName;
    private int packagingPrice;
    private String packagingImageUrl;
}
