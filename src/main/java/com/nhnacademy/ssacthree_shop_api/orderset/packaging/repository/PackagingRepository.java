package com.nhnacademy.ssacthree_shop_api.orderset.packaging.repository;

import com.nhnacademy.ssacthree_shop_api.orderset.packaging.domain.Packaging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackagingRepository extends JpaRepository<Packaging, Long> {
}
