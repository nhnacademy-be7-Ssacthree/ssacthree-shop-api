package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookCommonService;
import com.nhnacademy.ssacthree_shop_api.commons.exception.NotFoundException;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.dto.OrderDetailSaveRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepositoryCustom;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.domain.OrderDetail;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.dto.OrderDetailDTO;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.dto.OrderDetailResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.repo.OrderDetailRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.service.OrderDetailService;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetailpackaging.domain.OrderDetailPackaging;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetailpackaging.domain.repository.OrderDetailPackagingRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.domain.Packaging;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.repository.PackagingRepository;

import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.Payment;
import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final BookCommonService bookCommonService;
    private final BookRepository bookRepository;
    private final PackagingRepository packagingRepository;
    private final OrderDetailPackagingRepository orderDetailPackagingRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;
    private final PaymentRepository paymentRepository;


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE) // 재고차감 때문에
    public void saveOrderDetails(Order order, List<OrderDetailSaveRequest> orderDetailList) {
        // 주문 상세 요청 -> 주문 상세 만들기.. 포인트 등등도 모두 .. 저장
        List<OrderDetail> orderDetails = new ArrayList<>();
        List<OrderDetailPackaging> orderDetailPackagingList = new ArrayList<>();

        for (OrderDetailSaveRequest orderDetailSaveRequest : orderDetailList) {
            // 책 수량 확인
            BookInfoResponse bookInfo = bookCommonService.getBook(orderDetailSaveRequest.getBookId());
            if (bookInfo.getStock() - orderDetailSaveRequest.getQuantity() < 0) {
                throw new RuntimeException("재고가 부족합니다.");
            }

            // 재고 차감 - 배타락 이용 (읽기, 쓰기 동시 불가하게)
//            Book book = bookRepository.findOne(orderDetailSaveRequest.getBookId());

            // TODO : 도서 상세당 쿠폰 사용 처리
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



            //TODO : 포장 정보 저장 - 수량은 일단 1로 설정
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

    // orderNumber로 orderId 조회
    @Override
    public Optional<Long> getOrderId(String orderNumber) {
        Optional<Long> orderId = orderRepositoryCustom.findOrderIdByOrderNumber(orderNumber);
        return orderId;
    }


    // 주문 내역의 구매자 전화번호와 입력 된 전화번호가 일치하는지 확인
    @Override
    public Boolean comparePhoneNumber(Long orderId,String phoneNumber){
        // orderId로 주문 -> customer -> customerPhone 접근
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order order;
        if (optionalOrder.isPresent()) {    // null 아니면
            order = optionalOrder.get();
        } else {
            throw new NotFoundException("Order not found with id: " + orderId);
        }

        // 구매자의 전화번호와 동일한가?
        String buyerPhoneNumber = order.getCustomer().getCustomerPhoneNumber();
        log.info("구매자의 전화번호입니다: {} , 입력 된 전화번호: {}", buyerPhoneNumber, phoneNumber);
        return buyerPhoneNumber.equals(phoneNumber);
    }



    // 주문 상세 조회 (주문+주문상세+결제내역)
    // 각 service에 API 없어서 직접 repo 사용
    /* 필요 repo
     *   1. Order / 2. OrderDetail / 3. DeliveryRule / 4. Book / 5. Payment / 6. PaymentStatus / 7. Coupon(제외)
     */
    @Override
    public OrderDetailResponse getOrderDetail(Long orderId) {
        // 1. 주문 정보 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("해당 주문을 찾을 수 없습니다: " + orderId));


        // 2. 주문 상세 정보 조회

        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        log.info("주문상세조회성공");
        List<OrderDetailDTO> orderDetailList = orderDetails.stream()
                .map(orderDetail -> new OrderDetailDTO(
                        orderDetail.getBook().getBookId(),
                        orderDetail.getBook().getBookName(),
                        orderDetail.getBook().getBookThumbnailImageUrl(),
                        orderDetail.getQuantity(),
                        orderDetail.getBookpriceAtOrder()
                ))
                .toList();

        // log 확인
        orderDetailList.forEach(orderDetail ->
                log.info("OrderDetailDTO - BookId: {}, BookName: {}, bookThumbnailIMG: {}, Quantity: {}, BookPriceAtOrder: {}",
                        orderDetail.getBookId(),
                        orderDetail.getBookName(),
                        orderDetail.getBookThumbnailImageUrl(),
                        orderDetail.getQuantity(),
                        orderDetail.getBookPriceAtOrder())
        );


        log.info("결제정보를 조회합니다.");


        // 3. 결제 정보 조회
        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new EntityNotFoundException("결제 정보를 찾을 수 없습니다: " + orderId));
        log.info("payment내용: {} / {}", payment.getId(), payment.getPaymentKey());
        String paymentTypeName = payment.getPaymentType().getPaymentTypeName();

        // 4. 배송 정책 조회
        DeliveryRule deliveryRule = order.getDeliveryRuleId();
        int deliveryFee = deliveryRule.getDeliveryFee();

        // 5. DTO 매핑
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse(
                order.getId(),
                order.getOrdered_date().toLocalDate(),
                order.getOrder_number(),
                order.getDeliveryDate(),
                "읏짜", // 송장 번호 (별도로 저장되어 있다면 추가 가능)
                order.getReceiverName(),
                order.getReceiverPhone(),
                order.getOrderRequest(),
                order.getRoadAddress(),
                order.getDetailAddress(),
                order.getPostalCode(),
                order.getTotal_price(),
                deliveryFee,
                orderDetailList,
                payment.getId(),
                payment.getPaymentCreatedAt(),
                payment.getPaymentAmount(),
                payment.getPaymentKey(),
                convertPaymentStatus(payment),
                paymentTypeName
        );
        return orderDetailResponse;
    }
    public String convertPaymentStatus(Payment payment){
        log.info("payment상태변환");
        if(payment.getPaymentStatus().name().equals("DONE")){
            return "결제 완료";
        }else{
            return "결제 취소";
        }
    }


}