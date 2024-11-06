package com.nhnacademy.ssacthree_shop_api.memberset.address.service.impl;

import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.address.domain.Address;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressCreateRequest;
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
    public Address createAddress(long memberId,AddressCreateRequest addressCreateRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));
        Address address = new Address(
            member,
            addressCreateRequest.getAddressAlias(),
            addressCreateRequest.getAddressDetail(),
            addressCreateRequest.getAddressPostalNumber()
        );
        return addressRepository.save(address);
    }
}
