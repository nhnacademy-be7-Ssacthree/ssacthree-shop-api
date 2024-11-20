package com.nhnacademy.ssacthree_shop_api.review.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.review.domain.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByBook(Book book);

    Optional<Review> findReviewByBookAndOrder(Book book, Order order);

}
