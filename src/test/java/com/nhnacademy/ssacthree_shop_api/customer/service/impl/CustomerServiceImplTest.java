package com.nhnacademy.ssacthree_shop_api.customer.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerCreateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerGetResponse;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.exception.CustomerNotFoundException;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer() {
        CustomerCreateRequest createRequest = new CustomerCreateRequest("John Doe", "010-1234-5678",
            "johndoe@example.com");
        Customer savedCustomer = new Customer(1L, "John Doe", "johndoe@example.com",
            "010-1234-5678");

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        Customer createdCustomer = customerService.createCustomer(createRequest);

        assertNotNull(createdCustomer);
        assertEquals(savedCustomer.getCustomerId(), createdCustomer.getCustomerId());
        assertEquals(savedCustomer.getCustomerName(), createdCustomer.getCustomerName());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_Success() {
        long customerId = 1L;
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("John Smith",
            "010-9876-5432", "johnsmith@example.com");
        Customer existingCustomer = new Customer(customerId, "John Doe", "johndoe@example.com",
            "010-1234-5678");

        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        customerService.updateCustomer(customerId, updateRequest);

        assertEquals("John Smith", existingCustomer.getCustomerName());
        assertEquals("johnsmith@example.com", existingCustomer.getCustomerEmail());
        assertEquals("010-9876-5432", existingCustomer.getCustomerPhoneNumber());
        verify(customerRepository).save(existingCustomer);
    }

    @Test
    void testUpdateCustomer_NotFound() {
        long customerId = 1L;
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("John Smith",
            "010-9876-5432", "johnsmith@example.com");

        when(customerRepository.existsById(customerId)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class,
            () -> customerService.updateCustomer(customerId, updateRequest));
    }

    @Test
    void testDeleteCustomerById_Success() {
        long customerId = 1L;

        when(customerRepository.existsById(customerId)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(customerId);

        customerService.deleteCustomerById(customerId);

        verify(customerRepository).deleteById(customerId);
    }

    @Test
    void testDeleteCustomerById_NotFound() {
        long customerId = 1L;

        when(customerRepository.existsById(customerId)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class,
            () -> customerService.deleteCustomerById(customerId));
    }

    @Test
    void testGetCustomerById_Success() {
        long customerId = 1L;
        Customer foundCustomer = new Customer(customerId, "John Doe", "johndoe@example.com",
            "010-1234-5678");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(foundCustomer));

        CustomerGetResponse response = customerService.getCustomerById(customerId);

        assertNotNull(response);
        assertEquals(foundCustomer.getCustomerId(), response.getCustomerId());
        assertEquals(foundCustomer.getCustomerName(), response.getCustomerName());
        verify(customerRepository).findById(customerId);
    }

    @Test
    void testGetCustomerById_NotFound() {
        long customerId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
            () -> customerService.getCustomerById(customerId));
    }
}
