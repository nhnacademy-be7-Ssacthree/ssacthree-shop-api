package com.nhnacademy.ssacthree_shop_api.customer.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerCreateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<MessageResponse> createCustomer(@RequestBody CustomerCreateRequest customerCreateRequest) {
        customerService.createCustomer(customerCreateRequest);

        MessageResponse messageResponse = new MessageResponse("생성 성공");

    }
}
