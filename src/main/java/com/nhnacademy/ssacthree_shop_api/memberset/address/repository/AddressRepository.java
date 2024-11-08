package com.nhnacademy.ssacthree_shop_api.memberset.address.repository;

import com.nhnacademy.ssacthree_shop_api.memberset.address.domain.Address;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByMember(Member member);
    Address findAddressByRegisteredAddressId(long id);
    Optional<Address> findByAddressRoadnameAndMember(String addressRoadname, Member member);
}
