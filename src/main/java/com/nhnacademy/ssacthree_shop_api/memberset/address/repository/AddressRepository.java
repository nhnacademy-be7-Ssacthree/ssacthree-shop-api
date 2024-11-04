package com.nhnacademy.ssacthree_shop_api.memberset.address.repository;

import com.nhnacademy.ssacthree_shop_api.memberset.address.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
