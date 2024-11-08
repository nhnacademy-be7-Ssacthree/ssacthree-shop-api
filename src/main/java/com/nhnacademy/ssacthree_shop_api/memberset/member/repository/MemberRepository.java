package com.nhnacademy.ssacthree_shop_api.memberset.member.repository;

import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberLoginId(String loginId);
    boolean existsByMemberLoginId(String loginId);
}
