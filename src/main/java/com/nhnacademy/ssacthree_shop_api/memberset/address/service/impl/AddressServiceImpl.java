package com.nhnacademy.ssacthree_shop_api.memberset.address.service.impl;

import com.nhnacademy.ssacthree_shop_api.memberset.address.domain.Address;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.address.exception.DuplicateAddressException;
import com.nhnacademy.ssacthree_shop_api.memberset.address.exception.OverAddressException;
import com.nhnacademy.ssacthree_shop_api.memberset.address.repository.AddressRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.address.service.AddressService;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
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
        Optional<Address> existingAddress = addressRepository.findByAddressRoadnameAndMember(addressCreateRequest.getAddressRoadname(),member);

        if (existingAddress.isPresent()) {
            throw new DuplicateAddressException("해당 도로명 주소는 이미 존재합니다.");
        }

        if(addressRepository.findAllByMember(member).size() >= 10){
            throw new OverAddressException("도로명 주소가 10개 이상입니다!");
        }


        addressRepository.save(address);
        return new AddressResponse(
            address.getRegisteredAddressId(),
            address.getAddressAlias(),
            address.getAddressDetail(),
            address.getAddressRoadname(),
            address.getAddressPostalNumber()
        );
    }

    @Override
    public List<AddressResponse> getAddressesByUserId(String userId) {
        Member member = memberRepository.findByMemberLoginId(userId)
            .orElseThrow(() -> new MemberNotFoundException("member not found"));

        // Address 엔티티를 AddressResponse DTO로 변환
        return addressRepository.findAllByMember(member).stream()
            .map(address -> new AddressResponse(
                address.getRegisteredAddressId(),
                address.getAddressAlias(),
                address.getAddressRoadname(),
                address.getAddressDetail(),
                address.getAddressPostalNumber()
            ))
            .toList();
    }

    @Override
    public void deleteAddressById(String addressId) {
        long id = Long.parseLong(addressId);
        Address address = addressRepository.findAddressByRegisteredAddressId(id);
        addressRepository.delete(address);
    }

}
