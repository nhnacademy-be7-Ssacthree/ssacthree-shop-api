package com.nhnacademy.ssacthree_shop_api.memberset.address.service.impl;

import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.address.domain.Address;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.address.repository.AddressRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public Address createAddress(AddressCreateRequest addressCreateRequest) {
        Address address = new Address(
            addressCreateRequest.getMember(),
            addressCreateRequest.getAddressAlias(),
            addressCreateRequest.getAddressDetail(),
            addressCreateRequest.getAddressPostalNumber()
        );
    }
}
