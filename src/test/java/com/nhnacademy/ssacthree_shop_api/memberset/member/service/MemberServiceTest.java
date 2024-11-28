package com.nhnacademy.ssacthree_shop_api.memberset.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.service.CustomerService;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.MemberStatus;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberRegisterRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.member.event.MemberRegisteredEvent;
import com.nhnacademy.ssacthree_shop_api.memberset.member.exception.AlreadyMemberException;
import com.nhnacademy.ssacthree_shop_api.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberCustomRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.member.service.impl.MemberServiceImpl;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.repository.MemberGradeRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.service.PointHistoryService;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveType;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.repository.PointSaveRuleRepository;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
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
    private MemberCustomRepository memberCustomRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PointHistoryService pointHistoryService;

    @Mock
    private PointSaveRuleRepository pointSaveRuleRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    void registerMember() {
        // given
        MemberRegisterRequest dummy = new MemberRegisterRequest(
            "xxx123", "password123", "xxx", "010-1111-111", "xxx@naver.com", "20000101");

        Customer customer = new Customer();
        when(customerService.createCustomer(any())).thenReturn(customer);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        MemberGrade memberGrade = new MemberGrade();
        when(memberGradeRepository.findById(1L)).thenReturn(Optional.of(memberGrade));

        PointSaveRule pointSaveRule = new PointSaveRule("회원가입", 1000, PointSaveType.INTEGER);
        Member member = new Member();
        PointHistory pointHistory = new PointHistory(pointSaveRule, member);
        // when
        when(pointSaveRuleRepository.findPointSaveRuleByPointSaveRuleName("회원가입")).thenReturn(
            Optional.of(pointSaveRule));
        when(
            pointHistoryService.savePointHistory(
                any(PointSaveRule.class),
                any(Member.class),
                any(PointHistorySaveRequest.class)
            )
        ).thenReturn(pointHistory);
        memberService.registerMember(dummy);

        // then
        verify(customerService).createCustomer(any());
        verify(passwordEncoder).encode("password123");
        verify(memberGradeRepository).findById(1L);
        verify(memberRepository).save(any(Member.class));
        verify(applicationEventPublisher).publishEvent(any(MemberRegisteredEvent.class));
    }

    @Test
    void registerMemberAlreadyMemberException() {
        MemberRegisterRequest dummy = new MemberRegisterRequest(
            "xxx123", "password123", "xxx", "010-1111-111", "xxx@naver.com", "20000101");

        when(memberRepository.existsByMemberLoginId(dummy.getLoginId())).thenReturn(true);

        assertThrows(AlreadyMemberException.class, () -> memberService.registerMember(dummy));
    }

    @Test
    void getMemberInfoByIdTest() {
        MemberInfoGetResponse dummy = new MemberInfoGetResponse();
        when(memberCustomRepository.getMemberWithCustomer(any())).thenReturn(dummy);

        assertEquals(dummy, memberService.getMemberInfoById(dummy.getMemberLoginId()));
    }

    @Test
    void updateMember_success() {
        // Given
        String memberLoginId = "testLoginId";
        MemberInfoUpdateRequest updateRequest = new MemberInfoUpdateRequest(
            "Updated Name", "010-1234-5678", "updatedEmail@example.com"
        );

        Customer customer = new Customer();
        customer.setCustomerName("Old Name");
        customer.setCustomerPhoneNumber("010-9876-5432");
        customer.setCustomerEmail("oldEmail@example.com");

        Member member = new Member();
        member.setCustomer(customer);

        // Stubbing
        when(memberRepository.findByMemberLoginId(memberLoginId)).thenReturn(Optional.of(member));

        // When
        MessageResponse response = memberService.updateMember(memberLoginId, updateRequest);

        // Then
        assertEquals("생성 성공", response.getMessage());
        assertEquals("Updated Name", customer.getCustomerName());
        assertEquals("010-1234-5678", customer.getCustomerPhoneNumber());
        assertEquals("updatedEmail@example.com", customer.getCustomerEmail());
        verify(memberRepository).findByMemberLoginId(memberLoginId);
        verify(memberRepository).save(member);
    }

    @Test
    void updateMember_memberNotFound() {
        // Given
        String memberLoginId = "nonExistingId";
        MemberInfoUpdateRequest updateRequest = new MemberInfoUpdateRequest(
            "Updated Name", "010-1234-5678", "updatedEmail@example.com"
        );

        // Stubbing
        when(memberRepository.findByMemberLoginId(memberLoginId)).thenReturn(Optional.empty());

        // When & Then
        MemberNotFoundException exception = assertThrows(
            MemberNotFoundException.class,
            () -> memberService.updateMember(memberLoginId, updateRequest)
        );

        assertEquals("멤버를 찾을 수 없습니다.", exception.getMessage());
        verify(memberRepository).findByMemberLoginId(memberLoginId);
        verify(memberRepository, never()).save(any());
    }

    @Test
    void deleteMember_success() {
        // Given
        String memberLoginId = "testLoginId";

        Member member = new Member();

        member.setMemberStatus(MemberStatus.ACTIVE);

        // Stubbing
        when(memberRepository.findByMemberLoginId(memberLoginId)).thenReturn(Optional.of(member));

        // When
        MessageResponse response = memberService.deleteMember(memberLoginId);

        // Then
        assertEquals("삭제 성공", response.getMessage());
        assertEquals(MemberStatus.WITHDRAW, member.getMemberStatus());
        verify(memberRepository).findByMemberLoginId(memberLoginId);
        verify(memberRepository).save(member);
    }

    @Test
    void deleteMember_memberNotFound() {
        // Given
        String memberLoginId = "nonExistingId";

        // Stubbing
        when(memberRepository.findByMemberLoginId(memberLoginId)).thenReturn(Optional.empty());

        // When & Then
        MemberNotFoundException exception = assertThrows(
            MemberNotFoundException.class,
            () -> memberService.deleteMember(memberLoginId)
        );

        assertEquals("멤버를 찾을 수 없습니다.", exception.getMessage());
        verify(memberRepository).findByMemberLoginId(memberLoginId);
        verify(memberRepository, never()).save(any());
    }

    @Test
    void getCustomerIdByMemberLoginId_success()
        throws NoSuchFieldException, IllegalAccessException {
        // Given
        String memberLoginId = "testLoginId";
        long expectedCustomerId = 123L;

        Customer customer = new Customer();
        customer.setCustomerId(expectedCustomerId);

        Member member = new Member(
            customer,
            memberLoginId,
            "password123",
            "19900101"
        );

        // Reflection으로 id 값 설정
        Field idField = Member.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(member, expectedCustomerId);

        // Stubbing
        when(memberRepository.findByMemberLoginId(memberLoginId)).thenReturn(Optional.of(member));

        // When
        Long actualCustomerId = memberService.getCustomerIdByMemberLoginId(memberLoginId);

        // Then
        assertEquals(expectedCustomerId, actualCustomerId);
        verify(memberRepository).findByMemberLoginId(memberLoginId);
    }

}
