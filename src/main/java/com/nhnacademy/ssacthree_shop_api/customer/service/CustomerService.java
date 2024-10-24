package com.nhnacademy.ssacthree_shop_api.customer.service;


import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerCreateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerGetResponse;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;

public interface CustomerService {

    ResponseEntity<MessageResponse> createCustomer(
        CustomerCreateRequest customerCreateRequest);

    void updateCustomer(
        Long customerId,
        CustomerUpdateRequest customerUpdateRequest);

    ResponseEntity<MessageResponse> deleteCustomerById(Long customerId);

    ResponseEntity<CustomerGetResponse> getCustomerById(Long customerId);
}
