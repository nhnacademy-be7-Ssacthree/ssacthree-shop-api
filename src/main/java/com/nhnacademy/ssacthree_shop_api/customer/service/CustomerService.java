package com.nhnacademy.ssacthree_shop_api.customer.service;


import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerCreateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerGetResponse;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;


public interface CustomerService {

    Customer createCustomer(
        CustomerCreateRequest customerCreateRequest);

    void updateCustomer(
        Long customerId,
        CustomerUpdateRequest customerUpdateRequest);

    void deleteCustomerById(Long customerId);

    CustomerGetResponse getCustomerById(Long customerId);
}
