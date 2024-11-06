package com.nhnacademy.ssacthree_shop_api.memberset.address.controller;

import com.nhnacademy.ssacthree_shop_api.memberset.address.domain.Address;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/shop/members")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/address")
    public ResponseEntity<Address> createAddress(
        @RequestHeader(name = "X-USER-ID") String memberId,
        @Validated @RequestBody AddressCreateRequest addressRequest) {

        Address savedAddress = addressService.createAddress(Long.parseLong(memberId), addressRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
    }

}
