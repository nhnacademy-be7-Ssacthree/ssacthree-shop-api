package com.nhnacademy.ssacthree_shop_api.customer.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerCreateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerGetResponse;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateCustomer() throws Exception {
        CustomerCreateRequest createRequest = new CustomerCreateRequest(
            "John Doe", "010-1234-5678", "johndoe@example.com");
        long createdCustomerId = 1L;

        when(customerService.createCustomer(any())).thenReturn(
            new Customer(createdCustomerId, "John Doe", "johndoe@example.com", "010-1234-5678"));

        mockMvc.perform(post("/api/shop/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated());

        verify(customerService).createCustomer(any(CustomerCreateRequest.class));
    }

    @Test
    void testGetCustomerById() throws Exception {
        long customerId = 1L;
        CustomerGetResponse response = new CustomerGetResponse(customerId, "John Doe",
            "010-1234-5678", "johndoe@example.com");

        when(customerService.getCustomerById(customerId)).thenReturn(response);

        mockMvc.perform(get("/api/shop/customers/{customerId}", customerId))
            .andExpect(status().isOk());

        verify(customerService).getCustomerById(customerId);
    }

    @Test
    void testDeleteCustomer() throws Exception {
        long customerId = 1L;
        MessageResponse response = new MessageResponse(customerId + " - 삭제 완료");

        doNothing().when(customerService).deleteCustomerById(customerId);

        mockMvc.perform(delete("/api/shop/customers/{customerId}", customerId))
            .andExpect(status().isOk());

        verify(customerService).deleteCustomerById(customerId);
    }

    @Test
    void testUpdateCustomer() throws Exception {
        long customerId = 1L;
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
            "John Smith", "010-9876-5432", "johnsmith@example.com");
        MessageResponse response = new MessageResponse(customerId + "가 수정 되었습니다.");

        doNothing().when(customerService)
            .updateCustomer(eq(customerId), any(CustomerUpdateRequest.class));

        mockMvc.perform(put("/api/shop/customers/{customerId}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk());

        verify(customerService).updateCustomer(eq(customerId), any(CustomerUpdateRequest.class));
    }
}
