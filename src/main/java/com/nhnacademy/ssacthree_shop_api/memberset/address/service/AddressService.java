package com.nhnacademy.ssacthree_shop_api.memberset.address.service;

import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressResponse;

public interface AddressService {

    AddressResponse createAddress(String header, AddressCreateRequest addressCreateRequest);

}
