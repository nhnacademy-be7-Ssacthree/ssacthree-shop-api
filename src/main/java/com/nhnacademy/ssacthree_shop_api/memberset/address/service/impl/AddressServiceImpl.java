package com.nhnacademy.ssacthree_shop_api.memberset.address.service.impl;

import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.address.domain.Address;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.address.repository.AddressRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.address.service.AddressService;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    @Override
    public AddressResponse createAddress(String header,AddressCreateRequest addressCreateRequest) {
        Member member = memberRepository.findByMemberLoginId(header).orElseThrow(() -> new MemberNotFoundException("member not found"));
        Address address = new Address(
            member,
            addressCreateRequest.getAddressAlias(),
            addressCreateRequest.getAddressDetail(),
            addressCreateRequest.getAddressRoadname(),
            addressCreateRequest.getAddressPostalNumber()
        );
        addressRepository.save(address);
        return new AddressResponse(
            address.getAddressAlias(),
            address.getAddressDetail(),
            address.getAddressRoadname(),
            address.getAddressPostalNumber()
        );
    }
}
