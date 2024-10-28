package com.nhnacademy.ssacthree_shop_api.customer.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerCreateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.exception.CustomerNotFoundException;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.customer.service.impl.CustomerServiceImpl;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeUpdateResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.exception.MemberGradeNotFoundException;
import java.util.Optional;
import org.hibernate.validator.constraints.ru.INN;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;


    @Test
    void createCustomer() {

        //given

        CustomerCreateRequest dummy = new CustomerCreateRequest("test", "010-1111-1111",
            "test@naver.com");

        //when
        customerService.createCustomer(dummy);
        //then
        verify(customerRepository).save(any(Customer.class));
    }


    @Test
    void updateCustomer() {

        //given

        CustomerUpdateRequest dummy = new CustomerUpdateRequest("test", "010-1111-1111",
            "test@naver.com");
        Customer existingDummy = new Customer("test", "010-1111-1111", "test@naver.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingDummy));
        when(customerRepository.existsById(1L)).thenReturn(true);
        // when
        customerService.updateCustomer(1L, dummy);
    }

    @Test
    void updateCustomerExceptionTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            customerService.updateCustomer(-1L, null);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            customerService.updateCustomer(0L, null);
        });

        Assertions.assertThrows(CustomerNotFoundException.class, () -> {
            when(customerRepository.existsById(1L)).thenReturn(false);
            customerService.updateCustomer(1L, null);
        });
    }

    @Test
    void deleteCustomer() {

        //given
        Long customerId = 1L;
        Customer existingDummy = new Customer("test", "010-1111-1111", "test@naver.com");
        when(customerRepository.existsById(1L)).thenReturn(true);

        //when
        customerService.deleteCustomerById(customerId);

        //then
        verify(customerRepository).deleteById(1L);

    }

    @Test
    void deleteCustomerException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            customerService.deleteCustomerById(-1L);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            customerService.deleteCustomerById(0L);
        });

        Assertions.assertThrows(CustomerNotFoundException.class, () -> {
            when(customerRepository.existsById(1L)).thenReturn(false);
            customerService.deleteCustomerById(1L);
        });
    }

    @Test
    void getCustomerById() {
        //given
        Long customerId = 1L;
        Customer existingDummy = new Customer("test", "010-1111-1111", "test@naver.com");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingDummy));
        when(customerRepository.existsById(1L)).thenReturn(true);

        //when
        customerService.getCustomerById(1L);

        //then
        verify(customerRepository).findById(1L);
    }

    @Test
    void getMemberGradeByIdException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            customerService.getCustomerById(-1L);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            customerService.getCustomerById(0L);
        });

        Assertions.assertThrows(CustomerNotFoundException.class, () -> {
            when(customerRepository.existsById(1L)).thenReturn(false);
            customerService.getCustomerById(1L);
        });
    }
}
