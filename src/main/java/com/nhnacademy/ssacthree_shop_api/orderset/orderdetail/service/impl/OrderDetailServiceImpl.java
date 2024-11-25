package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookCommonService;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderDetailSaveRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.domain.OrderDetail;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.repo.OrderDetailRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service.OrderDetailService;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetailpackaging.domain.OrderDetailPackaging;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetailpackaging.domain.repository.OrderDetailPackagingRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.domain.Packaging;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.repository.PackagingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final BookCommonService bookCommonService;
    private final BookRepository bookRepository;
    private final PackagingRepository packagingRepository;
    private final OrderDetailPackagingRepository orderDetailPackagingRepository;


    @Override
    @Transactional
    public void saveOrderDetails(Order order, List<OrderDetailSaveRequest> orderDetailList) {
        // 주문 상세 요청 -> 주문 상세 만들기.. 포인트 등등도 모두 .. 저장
        List<OrderDetail> orderDetails = new ArrayList<>();
        List<OrderDetailPackaging> orderDetailPackagingList = new ArrayList<>();

        for (OrderDetailSaveRequest orderDetailSaveRequest : orderDetailList) {
            //책 수량 확인
            BookInfoResponse bookInfo = bookCommonService.getBook(orderDetailSaveRequest.getBookId());
            if (bookInfo.getStock() - orderDetailSaveRequest.getQuantity() < 0) {
                throw new RuntimeException("재고가 부족합니다.");
            }

            // TODO : 쿠폰 사용 처리
            // 일단은 null로 처리


            Book book = bookRepository.findByBookId(bookInfo.getBookId())
                    .orElseThrow(() -> new RuntimeException("책 없습니다."));


            //TODO : 주문 상세 리스트 저장
            OrderDetail orderDetail = new OrderDetail(
                    order,
                    book,
                    null,
                    orderDetailSaveRequest.getQuantity(),
                    orderDetailSaveRequest.getBookPriceAtOrder()
            );
            orderDetails.add(orderDetail);



            // TODO : 포장 정보 저장
            Packaging packaging = packagingRepository.findById(orderDetailSaveRequest.getPackagingId())
                    .orElseThrow(() -> new RuntimeException("포장 정보가 없습니다."));

            OrderDetailPackaging orderDetailPackaging = new OrderDetailPackaging(
                    null,
                    packaging,
                    order,
                    book,
                    1
            );
            orderDetailPackagingList.add(orderDetailPackaging);
        }

        //주문 상세 저장
        orderDetailRepository.saveAll(orderDetails);

        // 포장 정보 저장
        orderDetailPackagingRepository.saveAll(orderDetailPackagingList);


        //주문 상세 저장 후 뭐 반환?

    }
}
