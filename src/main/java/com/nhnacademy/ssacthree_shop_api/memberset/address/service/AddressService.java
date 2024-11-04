package com.nhnacademy.ssacthree_shop_api.memberset.address.service;

import com.nhnacademy.ssacthree_shop_api.memberset.address.domain.Address;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressCreateRequest;

public interface AddressService {

    Address createAddress(AddressCreateRequest addressCreateRequest);

}
