package com.nhnacademy.ssacthree_shop_api.orderset.packaging.service.impl;

import com.nhnacademy.ssacthree_shop_api.orderset.packaging.domain.Packaging;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto.PackagingGetResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.repository.PackagingRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.service.PackagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PackagingServiceImpl implements PackagingService {
    private final PackagingRepository packagingRepository;

    @Override
    public List<PackagingGetResponse> getAllPackaging() {
        List<Packaging> packaging = packagingRepository.findAll();
        //초기화 필요
        List<PackagingGetResponse> packagingGetResponses = new ArrayList<>();
        for (Packaging pack : packaging) {
            packagingGetResponses.add(new PackagingGetResponse(pack.getId(), pack.getPackagingName(), pack.getPackagingPrice(), pack.getPackagingImageUrl()));
        }
        return packagingGetResponses;
    }
}
