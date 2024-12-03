package com.nhnacademy.ssacthree_shop_api.customer.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerCreateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerGetResponse;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Long> createCustomer(
        @RequestBody CustomerCreateRequest customerCreateRequest) {
        Customer customer = customerService.createCustomer(customerCreateRequest);
        Long customerId = customer.getCustomerId();

        return ResponseEntity.status(HttpStatus.CREATED).body(customerId);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerGetResponse> getCustomerById(
        @PathVariable("customerId") Long customerId) {
        CustomerGetResponse foundCustomerGetResponse = customerService.getCustomerById(customerId);
        return ResponseEntity.ok().body(foundCustomerGetResponse);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<MessageResponse> deleteCustomer(
        @PathVariable("customerId") Long customerId) {
        customerService.deleteCustomerById(customerId);
        MessageResponse messageResponse = new MessageResponse(customerId + " - 삭제 완료");
        return ResponseEntity.ok().body(messageResponse);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<MessageResponse> updateCustomer(
        @PathVariable("customerId") Long customerId,
        @RequestBody CustomerUpdateRequest customerUpdateRequest) {

        customerService.updateCustomer(customerId, customerUpdateRequest);
        MessageResponse messageResponse = new MessageResponse(customerId + "가 수정 되었습니다.");
        return ResponseEntity.ok().body(messageResponse);
    }

}
