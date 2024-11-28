package com.nhnacademy.ssacthree_shop_api.review.service.impl;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.BookNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.repository.PointHistoryRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.exception.PointSaveRuleNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.repository.PointSaveRuleRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.exception.NotFoundOrderException;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.domain.OrderDetail;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.repo.OrderDetailRepository;
import com.nhnacademy.ssacthree_shop_api.review.domain.Review;
import com.nhnacademy.ssacthree_shop_api.review.domain.ReviewId;
import com.nhnacademy.ssacthree_shop_api.review.dto.MemberReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewRequestWithUrl;
import com.nhnacademy.ssacthree_shop_api.review.dto.BookReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.exception.ReviewNotFoundException;
import com.nhnacademy.ssacthree_shop_api.review.repository.ReviewCustomRepository;
import com.nhnacademy.ssacthree_shop_api.review.repository.ReviewRepository;
import com.nhnacademy.ssacthree_shop_api.review.service.ReviewService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository; //리뷰 찾기
    private final BookRepository bookRepository; //책 아이디로 책 찾기
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final PointSaveRuleRepository pointSaveRuleRepository;
    private final ReviewCustomRepository reviewCustomRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<BookReviewResponse> getReviewsByBookId(Pageable pageable, Long bookId) {
        // 책 존재 여부 확인
        bookRepository.findByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException("해당 책을 찾을 수 없습니다."));

        // QueryDSL을 이용한 리뷰 조회 및 페이징 처리
        return reviewCustomRepository.findReviewsByBookId(bookId, pageable);
    }

    @Override //리뷰 작성 저장
    public ResponseEntity<Void> postReviewBook(String header, Long bookId, Long orderId,
                                               ReviewRequestWithUrl reviewRequest) {
        Member member = memberRepository.findByMemberLoginId(header).orElseThrow(() -> new MemberNotFoundException("member not found"));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundOrderException("order not found"));
        Book book = bookRepository.findByBookId(bookId).orElseThrow(() -> new BookNotFoundException("book not found"));
        ReviewId reviewId = new ReviewId(orderId,bookId);
        Review review = new Review(reviewId,order,book,member.getCustomer(),reviewRequest.getReviewRate(),reviewRequest.getReviewTitle(),reviewRequest.getReviewContent(),reviewRequest.getReviewImageUrl());
        if(reviewRepository.findByReviewId(reviewId).isEmpty()) {
            PointHistory pointHistory = null;
            int memberPoint = 0;
            if(Objects.equals(reviewRequest.getReviewImageUrl(), "")){ //사진이 없는 경우
                PointSaveRule pointSaveRule = pointSaveRuleRepository.findPointSaveRuleByPointSaveRuleName("리뷰작성사진없음").orElseThrow(() -> new PointSaveRuleNotFoundException("point save rule not found"));
                pointHistory = new PointHistory(pointSaveRule,member,pointSaveRule.getPointSaveAmount(),"리뷰작성");
                memberPoint = member.getMemberPoint() + pointSaveRule.getPointSaveAmount();
            }else { // 사진이 있는 경우
                PointSaveRule pointSaveRule = pointSaveRuleRepository.findPointSaveRuleByPointSaveRuleName("리뷰작성사진있음").orElseThrow(() -> new PointSaveRuleNotFoundException("point save rule not found"));
                pointHistory = new PointHistory(pointSaveRule,member,pointSaveRule.getPointSaveAmount(),"리뷰작성");
                memberPoint = member.getMemberPoint() + pointSaveRule.getPointSaveAmount();
            }
            member.setMemberPoint(memberPoint);
            pointHistoryRepository.save(pointHistory);
        }
        reviewRepository.save(review);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Override //리뷰를 쓸 권한이 있는지 확인
    public Long authToWriteReview(String header, Long bookId) {
        Member member = memberRepository.findByMemberLoginId(header).orElseThrow(() -> new MemberNotFoundException("member not found"));
        List<Order> orderList = orderRepository.findOrderByCustomer(member.getCustomer());
        Book book = bookRepository.findByBookId(bookId).orElseThrow(() -> new BookNotFoundException("book not found"));
        for (Order order : orderList) {
            Optional<OrderDetail> auth = orderDetailRepository.findAllByBookAndOrder(book, order);
            Optional<Review> review = reviewRepository.findReviewByBookAndOrder(book,order);
            if(auth.isPresent() && review.isEmpty()) {
                return order.getId();
            }
        }
        return null;
    }
    @Override
    public List<MemberReviewResponse> getReviewsByMemberId(String header) {
        Member member = memberRepository.findByMemberLoginId(header).orElseThrow(() -> new MemberNotFoundException("member not found"));
        List<Review> reviews = reviewRepository.findAllByCustomer(member.getCustomer());
        List<MemberReviewResponse> reviewResponses = new ArrayList<>();
        for (Review review : reviews) {
            Book book = review.getBook();
            reviewResponses.add(new MemberReviewResponse(review.getReviewId().getOrderId(),review.getReviewId().getBookId(),book.getBookThumbnailImageUrl(),book.getBookName(),review.getReviewRate(),review.getReviewTitle(),review.getReviewContent(),review.getReviewImageUrl()));
        }
        return reviewResponses;
    }
    @Override
    public ReviewResponse getReview(String header, Long orderId, Long bookId) {
        ReviewId reviewId = new ReviewId(orderId,bookId);
        Review review = reviewRepository.findByReviewId(reviewId).orElseThrow(() -> new ReviewNotFoundException("review not found"));
        return new ReviewResponse(review.getReviewRate(),review.getReviewTitle(),review.getReviewContent(),review.getReviewImageUrl());
    }
}