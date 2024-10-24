package com.nhnacademy.ssacthree_shop_api.customer.service;


import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerCreateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerGetResponse;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;

public interface CustomerService {

    public ResponseEntity<MessageResponse> createCustomer(
        CustomerCreateRequest customerCreateRequest);

    public ResponseEntity<MessageResponse> updateCustomer(
        CustomerUpdateRequest customerUpdateRequest);

    public ResponseEntity<MessageResponse> deleteCustomer(Long customerId);

    public ResponseEntity<CustomerGetResponse> getCustomer(Long customerId);
}
