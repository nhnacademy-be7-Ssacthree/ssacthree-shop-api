package com.nhnacademy.ssacthree_shop_api.orderset.packaging.controller;

import com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto.PackagingGetResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.service.PackagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/packaging")
public class PackagingCustomerController {
    private final PackagingService packagingService;

    @GetMapping
    public ResponseEntity<List<PackagingGetResponse>> getAllPackaging() {
        List<PackagingGetResponse> packaging = packagingService.getAllPackaging();
        return ResponseEntity.ok().body(packaging);
    }
}