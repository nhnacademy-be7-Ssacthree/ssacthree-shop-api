package com.nhnacademy.ssacthree_shop_api.orderset.packaging.service.impl;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.domain.Packaging;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto.PackagingCreateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto.PackagingGetResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.exception.PackagingAlreadyExistsException;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.exception.PackagingNotFoundException;
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

    @Override
    public MessageResponse savePackaging(PackagingCreateRequest packagingCreateRequest) {
        if(packagingRepository.existsByPackagingName(packagingCreateRequest.getName())) {
            throw new PackagingAlreadyExistsException("포장지 이름이 이미 존재합니다.");
        }

        Packaging packaging = new Packaging(packagingCreateRequest.getName(), packagingCreateRequest.getPrice(), packagingCreateRequest.getImageUrl());
        packagingRepository.save(packaging);
        MessageResponse messageResponse = new MessageResponse("생성 성공");
        return messageResponse;
    }

    @Override
    public MessageResponse deletePackaging(String packagingId) {
        long id = Long.parseLong(packagingId);
        if (!packagingRepository.existsById(id)) {
            throw new PackagingNotFoundException("해당 포장지가 존재하지 않습니다.");
        }

        // TODO : 추후 상태만 is_used를 false로 만드는 소프트 삭제로 변경 필요,
        packagingRepository.deleteById(id);
        MessageResponse messageResponse = new MessageResponse("삭제 성공");
        return messageResponse;
    }

}
