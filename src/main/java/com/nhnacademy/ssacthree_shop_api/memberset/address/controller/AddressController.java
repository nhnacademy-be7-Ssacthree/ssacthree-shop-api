package com.nhnacademy.ssacthree_shop_api.memberset.address.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<MessageResponse> createCustomer(@RequestBody AddressCreateRequest addressCreateRequest) {
        addressService.createAddress(addressCreateRequest);

        MessageResponse messageResponse = new MessageResponse("주소 생성 성공");
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

}
