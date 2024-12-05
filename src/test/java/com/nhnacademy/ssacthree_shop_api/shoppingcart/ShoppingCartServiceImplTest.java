package com.nhnacademy.ssacthree_shop_api.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.BookNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.domain.ShoppingCart;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.domain.ShoppingCartId;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.dto.ShoppingCartItemResponse;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.dto.ShoppingCartRequest;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.repository.ShoppingCartRepository;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.service.impl.ShoppingCartServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShoppingCartServiceImplTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    void testSaveCart_DeleteAllCarts() {
        // Mock 데이터
        Customer mockCustomer = new Customer(1L, "Test Customer", "test@example.com", "010-1234-5678");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
        when(shoppingCartRepository.findAllByCustomer(mockCustomer)).thenReturn(List.of(
            new ShoppingCart(new ShoppingCartId(1L, 101L), mockCustomer, null, 1)
        ));

        // 테스트 실행: 빈 카트 리스트 전달
        shoppingCartService.saveCart(List.of(), 1L);

        // 검증: 카트 삭제 메서드 호출
        verify(shoppingCartRepository, times(1)).deleteAll(any(List.class));
    }

    @Test
    void testSaveCartonLogout_Success() {
        // Mock 데이터

        Customer mockCustomer = new Customer(1L, "Test Customer", "test@example.com", "010-1234-5678");
        Member mockMember = new Member(mockCustomer, "TestMember", "testLoginId", null);
        Book mockBook = new Book(101L, "Test Book", "Index", "Info", "ISBN123", null, 20000, 15000,
            true, 10, "image_url", 100, 25, null, null, null, null, null);
        ShoppingCartRequest cartRequest = new ShoppingCartRequest(101L, 2);

        mockMember.setCustomer(mockCustomer);
        when(memberRepository.findByMemberLoginId("header")).thenReturn(Optional.of(mockMember));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
        when(bookRepository.findByBookId(101L)).thenReturn(Optional.of(mockBook));
        when(shoppingCartRepository.findById(any(ShoppingCartId.class))).thenReturn(Optional.empty());

        // 테스트 실행
        shoppingCartService.saveCartonLogout("header", List.of(cartRequest));

        // 검증
        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    void testGetBookByBookId_ThrowsBookNotFoundException() {
        // Mock 데이터
        when(bookRepository.findByBookId(999L)).thenReturn(Optional.empty());

        // 테스트 실행 및 검증
        assertThrows(BookNotFoundException.class, () -> shoppingCartService.getBookByBookId(999L));
        verify(bookRepository, times(1)).findByBookId(999L);
    }

    @Test
    void testSaveCart_ThrowsMemberNotFoundException() {
        // Mock 데이터
        when(memberRepository.findByMemberLoginId("invalidHeader")).thenReturn(Optional.empty());

        // 리팩토링: 예외를 발생시킬 메서드 호출만 포함하도록 람다 단순화
        Executable executable = () -> {
            shoppingCartService.saveCartonLogout("invalidHeader", List.of());
        };

        // 테스트 실행 및 검증
        assertThrows(MemberNotFoundException.class, executable);

        verify(memberRepository, times(1)).findByMemberLoginId("invalidHeader");
    }

    @Test
    void testGetBookByBookId_Success() {
        // Mock 데이터
        Book mockBook = new Book(101L, "Test Book", "Index", "Info", "ISBN123", null, 20000, 15000,
            true, 10, "image_url", 100, 25, null, null, null, null, null);

        when(bookRepository.findByBookId(101L)).thenReturn(Optional.of(mockBook));

        // 테스트 실행
        ShoppingCartItemResponse response = shoppingCartService.getBookByBookId(101L);

        // 검증
        assertEquals(101L, response.getId());
        assertEquals("Test Book", response.getTitle());
        verify(bookRepository, times(1)).findByBookId(101L);
    }

    @Test
    void testSaveCart_Success() {
        // Mock 데이터
        Customer mockCustomer = new Customer(1L, "Test Customer", "test@example.com",
            "010-1234-5678");
        Book mockBook = new Book(101L, "Test Book", "Index", "Info", "ISBN123", null, 20000, 15000,
            true, 10, "image_url", 100, 25, null, null, null, null, null);
        ShoppingCartRequest cartRequest = new ShoppingCartRequest(101L, 2);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
        when(bookRepository.findByBookId(101L)).thenReturn(Optional.of(mockBook));
        when(shoppingCartRepository.findById(any(ShoppingCartId.class))).thenReturn(
            Optional.empty());

        // 테스트 실행
        shoppingCartService.saveCart(List.of(cartRequest), 1L);

        // 검증
        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    void testIsCartUnmatched_ReturnsTrueWhenNoMatch() {
        // Mock 데이터
        Customer mockCustomer = new Customer(1L, "Test Customer", "test@example.com", "010-1234-5678");
        ShoppingCart existingCart = new ShoppingCart(new ShoppingCartId(1L, 101L), mockCustomer, null, 1);
        ShoppingCartRequest cartRequest1 = new ShoppingCartRequest(102L, 1);
        ShoppingCartRequest cartRequest2 = new ShoppingCartRequest(103L, 1);

        // 테스트 실행
        boolean result = shoppingCartService.isCartUnmatched(existingCart, List.of(cartRequest1, cartRequest2), 1L);

        // 검증: 카트 ID가 일치하지 않으면 true 반환
        assertTrue(result);
    }


}

