package com.nhnacademy.ssacthree_shop_api.memberset.member.service.impl;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.dto.CustomerCreateRequest;
import com.nhnacademy.ssacthree_shop_api.customer.service.CustomerService;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberRegisterRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.member.service.MemberService;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.exception.MemberGradeNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.repository.MemberGradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final CustomerService customerService;
    private final MemberGradeRepository memberGradeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, CustomerService customerService, MemberGradeRepository memberGradeRepository) {
        this.memberRepository = memberRepository;
        this.customerService = customerService;
        this.memberGradeRepository = memberGradeRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void registerMember(MemberRegisterRequest memberRegisterRequest) {
        CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest(
            memberRegisterRequest.getCustomerName(),
            memberRegisterRequest.getCustomerPhoneNumber(),
            memberRegisterRequest.getCustomerEmail()
        );
        Customer customer = customerService.createCustomer(customerCreateRequest);
        memberRegisterRequest.setLoginPassword(passwordEncoder.encode(memberRegisterRequest.getLoginPassword()));
        Member member = new Member(
            customer,
            memberRegisterRequest.getLoginId(),
            memberRegisterRequest.getLoginPassword(),
            memberRegisterRequest.getBirth()
        );

        MemberGrade memberGrade = memberGradeRepository.findById(1L)
            .orElseThrow(() -> new MemberGradeNotFoundException("등급을 찾을 수 없습니다."));
        member.setMemberGrade(memberGrade);
        memberRepository.save(member);
    }

    @Override
    public void getMemberById(String memberLoginId) {

    }

//    @Override
//    public void updateMember(String memberLoginId, MemberUpdateRequest memberUpdateRequest) {
//
//    }

    @Override
    public void deleteMember(String memberLoginId) {

    }
}
