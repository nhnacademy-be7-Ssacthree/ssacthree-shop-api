package com.nhnacademy.ssacthree_shop_api.customer.service.impl;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerCreateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerGetResponse;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;


    @Override
    public ResponseEntity<MessageResponse> createCustomer(
        CustomerCreateRequest customerCreateRequest) {
        Customer customer = new Customer(
            customerCreateRequest.getCustomerName(),
            customerCreateRequest.getCustomerEmail(),
            customerCreateRequest.getCustomerPhoneNumber()
        );

    }

    @Override
    public ResponseEntity<MessageResponse> updateCustomer(
        CustomerUpdateRequest customerUpdateRequest) {
        return null;
    }

    @Override
    public ResponseEntity<MessageResponse> deleteCustomer(Long customerId) {
        return null;
    }

    @Override
    public ResponseEntity<CustomerGetResponse> getCustomer(Long customerId) {
        return null;
    }
}
