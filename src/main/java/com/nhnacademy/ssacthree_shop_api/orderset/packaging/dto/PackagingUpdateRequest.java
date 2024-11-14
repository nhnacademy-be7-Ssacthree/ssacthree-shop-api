package com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PackagingUpdateRequest {

    private String packagingName;

    private int packagingPrice;

    private String packagingImageUrl;
}
