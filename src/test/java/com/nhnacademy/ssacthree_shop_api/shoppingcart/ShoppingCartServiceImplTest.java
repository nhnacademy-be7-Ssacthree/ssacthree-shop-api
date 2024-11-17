package com.nhnacademy.ssacthree_shop_api.shoppingcart;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.BookNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.domain.ShoppingCart;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.domain.ShoppingCartId;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.dto.ShoppingCartItemResponse;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.dto.ShoppingCartRequest;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.repository.ShoppingCartRepository;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.service.impl.ShoppingCartServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

}

