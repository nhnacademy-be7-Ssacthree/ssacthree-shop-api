package com.nhnacademy.ssacthree_shop_api.orderset.packaging.service;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto.PackagingCreateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto.PackagingGetResponse;

import java.util.List;

public interface PackagingService {
    List<PackagingGetResponse> getAllPackaging();

    MessageResponse savePackaging(PackagingCreateRequest packagingCreateRequest);

    MessageResponse deletePackaging(String packagingId);
}
