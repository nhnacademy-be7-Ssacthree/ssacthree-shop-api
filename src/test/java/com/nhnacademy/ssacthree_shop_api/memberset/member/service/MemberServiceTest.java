package com.nhnacademy.ssacthree_shop_api.memberset.member.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.service.CustomerService;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberRegisterRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.member.service.impl.MemberServiceImpl;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.repository.MemberGradeRepository;
import java.util.Optional;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private MemberGradeRepository memberGradeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    void createMember() {
        // given
        MemberRegisterRequest dummy = new MemberRegisterRequest(
            "xxx123", "password123", "xxx", "010-1111-111", "xxx@naver.com", "20000101");

        Customer customer = new Customer();
        when(customerService.createCustomer(any())).thenReturn(customer);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        MemberGrade memberGrade = new MemberGrade();
        when(memberGradeRepository.findById(1L)).thenReturn(Optional.of(memberGrade));

        // when
        memberService.registerMember(dummy);

        // then
        verify(customerService).createCustomer(any());
        verify(passwordEncoder).encode("password123");
        verify(memberGradeRepository).findById(1L);
        verify(memberRepository).save(any(Member.class));
    }
}
