package com.nhnacademy.ssacthree_shop_api.customer.service.impl;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerCreateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerGetResponse;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.exception.CustomerNotFoundException;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private static final String NOT_FOUND = "를 찾을 수 없습니다";


    @Override
    public Customer createCustomer(
        CustomerCreateRequest customerCreateRequest) {
        Customer customer = new Customer(
            customerCreateRequest.getCustomerName(),
            customerCreateRequest.getCustomerEmail(),
            customerCreateRequest.getCustomerPhoneNumber()
        );
        return customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(
        Long customerId,
        CustomerUpdateRequest customerUpdateRequest) {

        if (customerId <= 0) {
            throw new IllegalArgumentException("customerId는 0이하일 수 없습니다.");
        }
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId + NOT_FOUND);
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
            throw new CustomerNotFoundException(customerId + NOT_FOUND);
        }
        customerRepository.deleteById(customerId);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerGetResponse getCustomerById(Long customerId) {
        if(customerId <= 0) {
            throw new IllegalArgumentException("memberGradeId는 0 이하일 수 없습니다.");
        }
        if(!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId + NOT_FOUND);
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
