package com.nhnacademy.ssacthree_shop_api.memberset.address.service;

import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressResponse;
import java.util.List;

public interface AddressService {

    AddressResponse createAddress(String header, AddressCreateRequest addressCreateRequest);

    List<AddressResponse> getAddressesByUserId(String header);

    void deleteAddressById(String addressId);
}
