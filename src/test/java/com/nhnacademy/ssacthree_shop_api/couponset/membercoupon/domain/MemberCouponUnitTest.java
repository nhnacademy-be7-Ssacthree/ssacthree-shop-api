package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.domain;

import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MemberCouponUnitTest {

  @Mock
  private Coupon mockCoupon;

  @Mock
  private Member mockMember;

  @Mock
  private Customer mockCustomer;  // Mocking the Customer object

  private MemberCoupon memberCoupon;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this); // Initialize mocks

    // Mock Coupon methods
    when(mockCoupon.getCouponId()).thenReturn(1L);
    when(mockCoupon.getCouponName()).thenReturn("Test Coupon");

    // Mock Customer methods
    when(mockCustomer.getCustomerId()).thenReturn(1L);
    when(mockCustomer.getCustomerName()).thenReturn("Test Customer");
    when(mockCustomer.getCustomerEmail()).thenReturn("test@example.com");
    when(mockCustomer.getCustomerPhoneNumber()).thenReturn("1234567890");

    // Mock Member methods
    when(mockMember.getId()).thenReturn(1L);
    when(mockMember.getMemberLoginId()).thenReturn("testLogin");
    when(mockMember.getCustomer()).thenReturn(mockCustomer);  // Returning mocked Customer

    // Initialize MemberCoupon with mock dependencies
    memberCoupon = new MemberCoupon(
        mockCoupon,
        mockMember,
        LocalDateTime.now().plusDays(10) // Setting the expiration date 10 days later
    );
  }

  @Test
  public void testMemberCouponCreation() {
    // Test the creation of the MemberCoupon entity
    assertEquals("Test Coupon", memberCoupon.getCoupon().getCouponName());  // Check if coupon name matches
    assertEquals("Test Customer", memberCoupon.getCustomer().getCustomer().getCustomerName()); // Check if customer name matches
    assertEquals("testLogin", memberCoupon.getCustomer().getMemberLoginId());  // Check if member login ID matches
    assertEquals(1L, memberCoupon.getCoupon().getCouponId()); // Ensure coupon ID is set correctly
    assertEquals(1L, memberCoupon.getCustomer().getId()); // Ensure customer ID is set correctly
    assertEquals(10, memberCoupon.getMemberCouponExpiredAt().getDayOfYear() - LocalDateTime.now().getDayOfYear()); // Check expiration date is 10 days later
  }

  @Test
  public void testCouponUsedAt() {
    // Test setting and getting memberCouponUsedAt
    LocalDateTime usedAt = LocalDateTime.now().plusDays(5);
    memberCoupon.setMemberCouponUsedAt(usedAt);

    assertEquals(usedAt, memberCoupon.getMemberCouponUsedAt()); // Ensure the usedAt date is correctly set
  }

  @Test
  public void testCouponExpiredAt() {
    // Test setting and getting memberCouponExpiredAt
    LocalDateTime expiredAt = LocalDateTime.now().plusDays(15);
    memberCoupon.setMemberCouponExpiredAt(expiredAt);

    assertEquals(expiredAt, memberCoupon.getMemberCouponExpiredAt()); // Ensure the expiration date is correctly set
  }
}
