package com.nhnacademy.ssacthree_shop_api.orderset.packaging.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto.PackagingCreateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto.PackagingGetResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto.PackagingUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.service.PackagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/admin/packaging")
public class PackagingController {
    private final PackagingService packagingService;

    @GetMapping
    public ResponseEntity<List<PackagingGetResponse>> getAllPackaging() {
        List<PackagingGetResponse> packaging= packagingService.getAllPackaging();
        return ResponseEntity.ok().body(packaging);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createPackaging(@RequestBody PackagingCreateRequest packagingCreateRequest) {
        MessageResponse messageResponse = packagingService.savePackaging(packagingCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @PutMapping("/{packaging-id}")
    public ResponseEntity<MessageResponse> updatePackaging(@PathVariable("packaging-id") String packagingId, @RequestBody PackagingUpdateRequest packagingUpdateRequest) {
        MessageResponse messageResponse = packagingService.updatePackaging(packagingId, packagingUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @DeleteMapping("/{packaging-id}")
    public ResponseEntity<MessageResponse> deletePackaging(@PathVariable(value = "packaging-id") String packagingId) {
        MessageResponse messageResponse = packagingService.deletePackaging(packagingId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(messageResponse);
    }
}
