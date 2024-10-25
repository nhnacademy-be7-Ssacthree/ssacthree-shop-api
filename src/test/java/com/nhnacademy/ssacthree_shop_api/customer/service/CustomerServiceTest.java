package com.nhnacademy.ssacthree_shop_api.customer.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerCreateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.customer.service.impl.CustomerServiceImpl;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeUpdateResponse;
import java.util.Optional;
import org.hibernate.validator.constraints.ru.INN;
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

        CustomerCreateRequest dummy = new CustomerCreateRequest("test","010-1111-1111","test@naver.com");

        //when
        customerService.createCustomer(dummy);
        //then
        verify(customerRepository).save(any(Customer.class));
    }


    @Test
    void updateCustomer() {

        //given

        CustomerUpdateRequest dummy = new CustomerUpdateRequest("test","010-1111-1111","test@naver.com");
        Customer existingDummy = new Customer("test","010-1111-1111","test@naver.com");



        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingDummy));
        when(customerRepository.existsById(1L)).thenReturn(true);

        // when
        customerService.updateCustomer(1L, dummy);



    }
}
