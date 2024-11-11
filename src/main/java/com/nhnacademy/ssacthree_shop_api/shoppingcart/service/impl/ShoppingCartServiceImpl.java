package com.nhnacademy.ssacthree_shop_api.shoppingcart.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.BookNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.domain.ShoppingCart;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.dto.ShoppingCartItemResponse;
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


    @Override
    public List<ShoppingCartItemResponse> getShoppingCartItemsBycustomerId(String header) {
        Member member = memberRepository.findByMemberLoginId(header)
            .orElseThrow(() -> new MemberNotFoundException("member not found"));

        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByCustomer_CustomerId(member.getId());

        return shoppingCarts.stream()
            .map(cart -> new ShoppingCartItemResponse(
                cart.getBook().getBookId(),               // 도서 ID
                cart.getBook().getBookName(),            // 도서 제목
                cart.getBookQuantity(),               // 도서 수량
                cart.getBook().getSalePrice(),            // 도서 가격
                cart.getBook().getBookThumbnailImageUrl()             // 도서 이미지
            ))
            .collect(Collectors.toList());
    }

    @Override
    public ShoppingCartItemResponse getBookByBookId(String bookId) {
        Book book = bookRepository.findByBookId(Long.valueOf(bookId))
            .orElseThrow(() -> new BookNotFoundException("book not found"));

        return new ShoppingCartItemResponse(book.getBookId(),book.getBookName(), 1, book.getSalePrice(), book.getBookThumbnailImageUrl());
    }
}
