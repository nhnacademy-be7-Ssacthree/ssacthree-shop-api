package com.nhnacademy.ssacthree_shop_api.shoppingcart.service.impl;

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
import com.nhnacademy.ssacthree_shop_api.shoppingcart.service.ShoppingCartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;


    @Override
    public List<ShoppingCartItemResponse> getShoppingCartItemsBycustomerId(String header) {
        Member member = memberRepository.findByMemberLoginId(header)
            .orElseThrow(() -> new MemberNotFoundException("member not found"));

        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByCustomer_CustomerId(
            member.getId());

        return shoppingCarts.stream()
            .map(cart -> new ShoppingCartItemResponse(
                cart.getBook().getBookId(),               // 도서 ID
                cart.getBook().getBookName(),            // 도서 제목
                cart.getBookQuantity(),               // 도서 수량
                cart.getBook().getSalePrice(),            // 도서 가격
                cart.getBook().getBookThumbnailImageUrl()  // 도서 이미지
            ))
            .toList();
    }

    @Override
    public ShoppingCartItemResponse getBookByBookId(Long bookId) {
        Book book = bookRepository.findByBookId(bookId)
            .orElseThrow(() -> new BookNotFoundException("book not found"));

        return new ShoppingCartItemResponse(book.getBookId(), book.getBookName(), 1,
            book.getSalePrice(), book.getBookThumbnailImageUrl());
    }

    @Override
    public void saveCart(List<ShoppingCartRequest> cartList, Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);

        // 1. 쇼핑 카트를 삭제해야 하는지 확인
        if (shouldDeleteAllCarts(cartList)) {
            deleteAllCartsForCustomer(customer);
            return;
        }

        // 2. 기존 카트와 요청된 카트를 비교해 삭제
        List<ShoppingCart> existingCarts = shoppingCartRepository.findAllByCustomer(customer);
        deleteUnmatchedCarts(existingCarts, cartList, customerId);

        // 3. 요청된 카트를 저장
        saveOrUpdateCarts(cartList, customerId, customer);
    }

    private boolean shouldDeleteAllCarts(List<ShoppingCartRequest> cartList) {
        return cartList == null || cartList.isEmpty();
    }

    private void deleteAllCartsForCustomer(Customer customer) {
        List<ShoppingCart> existingCarts = shoppingCartRepository.findAllByCustomer(customer);
        if (!existingCarts.isEmpty()) {
            shoppingCartRepository.deleteAll(existingCarts);
        }
    }

    private void deleteUnmatchedCarts(List<ShoppingCart> existingCarts, List<ShoppingCartRequest> cartList, Long customerId) {
        for (ShoppingCart existingCart : existingCarts) {
            if (isCartUnmatched(existingCart, cartList, customerId)) {
                shoppingCartRepository.delete(existingCart);
            }
        }
    }

    public boolean isCartUnmatched(ShoppingCart existingCart, List<ShoppingCartRequest> cartList,
        Long customerId) {
        for (ShoppingCartRequest cartRequest : cartList) {
            ShoppingCartId existingCartId = existingCart.getShoppingCartId();
            ShoppingCartId newCartId = new ShoppingCartId(customerId, cartRequest.getBookId());
            if (existingCartId.equals(newCartId)) {
                return false;
            }
        }
        return true;
    }

    private void saveOrUpdateCarts(List<ShoppingCartRequest> cartList, Long customerId, Customer customer) {
        for (ShoppingCartRequest cartItem : cartList) {
            Long bookId = cartItem.getBookId();
            Book book = bookRepository.findByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException("book not found"));
            ShoppingCartId cartId = new ShoppingCartId(customerId, bookId);

            ShoppingCart existingCart = shoppingCartRepository.findById(cartId).orElse(null);

            if (existingCart != null) {
                existingCart.addBookQuantity(cartItem.getQuantity());
                shoppingCartRepository.save(existingCart);
            } else {
                ShoppingCart newCart = new ShoppingCart(cartId, customer, book, cartItem.getQuantity());
                shoppingCartRepository.save(newCart);
            }
        }
    }

    @Override
    public void saveCartonLogout(String header, List<ShoppingCartRequest> cartList) {
        Member member = memberRepository.findByMemberLoginId(header)
            .orElseThrow(() -> new MemberNotFoundException("member not found"));
        Long customerId = member.getCustomer().getCustomerId();

        saveCart(cartList, customerId);
    }

}
