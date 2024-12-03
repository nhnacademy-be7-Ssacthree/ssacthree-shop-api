package com.nhnacademy.ssacthree_shop_api.memberset.address.controller;

import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.address.service.AddressService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<AddressResponse> createAddress(
        @RequestHeader(name = "X-USER-ID") String header,
        @Valid @RequestBody AddressCreateRequest addressRequest) {
        AddressResponse savedAddress = addressService.createAddress(header, addressRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
    }

    @GetMapping("/address")
    public ResponseEntity<List<AddressResponse>> getAllAddresses(
        @RequestHeader(name = "X-USER-ID") String header) {
        List<AddressResponse> addresses = addressService.getAddressesByUserId(header);
        return ResponseEntity.ok(addresses);
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<Void> deleteAddress(
        @PathVariable("addressId") String addressId) {

        addressService.deleteAddressById(addressId);

        return ResponseEntity.noContent().build();
    }

}
