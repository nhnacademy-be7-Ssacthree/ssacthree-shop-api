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
import java.util.stream.Collectors;
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
            .collect(Collectors.toList());
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

        if (cartList == null || cartList.isEmpty()) {
            // 고객의 모든 쇼핑 카트 삭제
            List<ShoppingCart> existingCarts = shoppingCartRepository.findAllByCustomer(customer);
            if (!existingCarts.isEmpty()) {
                shoppingCartRepository.deleteAll(existingCarts);
            }
            return;
        }

        List<ShoppingCart> existingCartList = shoppingCartRepository.findAllByCustomer(customer);
        for (ShoppingCart shoppingCart : existingCartList) {
            boolean same = false;
            for (ShoppingCartRequest shoppingCartRequest : cartList) {
                ShoppingCartId existShoppingCartId = shoppingCart.getShoppingCartId();
                ShoppingCartId newShoppingCartId = new ShoppingCartId(customerId,
                    shoppingCartRequest.getBookId());
                if (existShoppingCartId.equals(newShoppingCartId)) {
                    same = true;
                }
            }
            if (!same) {
                shoppingCartRepository.delete(shoppingCart);
            }
        }

        for (ShoppingCartRequest cartItem : cartList) {
            // 필요한 개수와 항목 정보 추출
            int quantity = cartItem.getQuantity();
            Long bookId = cartItem.getBookId();
            Book book = bookRepository.findByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException("book not found"));
            ShoppingCartId shoppingCartId = new ShoppingCartId(customerId, bookId);

            // ShoppingCartRepository에서 해당 ID로 조회
            ShoppingCart existingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElse(null);

            if (existingCart != null) {
                // 존재할 경우 수량 업데이트
                existingCart.addBookQuantity(quantity);
                shoppingCartRepository.save(existingCart);
            } else {
                // 존재하지 않을 경우 새로 생성
                ShoppingCart newCart = new ShoppingCart(shoppingCartId, customer, book,
                    quantity);
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
