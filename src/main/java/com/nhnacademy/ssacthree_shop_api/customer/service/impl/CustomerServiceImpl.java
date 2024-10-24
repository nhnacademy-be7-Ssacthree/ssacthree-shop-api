package com.nhnacademy.ssacthree_shop_api.customer.service.impl;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerCreateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerGetResponse;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.exception.CustomerNotFoundException;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;


    @Override
    public void createCustomer(
        CustomerCreateRequest customerCreateRequest) {
        Customer customer = new Customer(
            customerCreateRequest.getCustomerName(),
            customerCreateRequest.getCustomerEmail(),
            customerCreateRequest.getCustomerPhoneNumber()
        );
        customerRepository.save(customer);

    }

    @Override
    public void updateCustomer(
        Long customerId,
        CustomerUpdateRequest customerUpdateRequest) {

        if (customerId <= 0) {
            throw new IllegalArgumentException("customerId는 0이하일 수 없습니다.");
        }
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId + "를 찾을 수 없습니다.");
        }

        Customer customer = customerRepository.findById(customerId).orElse(null);
        customer.setCustomerName(customerUpdateRequest.getCustomerName());
        customer.setCustomerEmail(customerUpdateRequest.getCustomerEmail());
        customer.setCustomerPhoneNumber(customerUpdateRequest.getCustomerPhoneNumber());
        customerRepository.save(customer);

    }

    @Override
    public void deleteCustomerById(Long customerId) {
        if(customerId <= 0) {
            throw new IllegalArgumentException("memberGradeId는 0 이하일 수 없습니다.");
        }
        if(!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId + "를 찾을 수 없습니다.");
        }
        customerRepository.deleteById(customerId);
    }

    @Override
    public CustomerGetResponse getCustomerById(Long customerId) {
        if(customerId <= 0) {
            throw new IllegalArgumentException("memberGradeId는 0 이하일 수 없습니다.");
        }
        if(!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId + "를 찾을 수 없습니다.");
        }
        Customer foundCustomer = customerRepository.findById(customerId).orElse(null);
        return new CustomerGetResponse(
            foundCustomer.getCustomerId(),
            foundCustomer.getCustomerName(),
            foundCustomer.getCustomerPhoneNumber(),
            foundCustomer.getCustomerEmail()
        );
    }
}
