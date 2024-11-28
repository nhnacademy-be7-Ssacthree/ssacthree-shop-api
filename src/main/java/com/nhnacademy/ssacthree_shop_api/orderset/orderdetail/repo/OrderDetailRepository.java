package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.repo;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.domain.OrderDetail;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    List<OrderDetail> findByOrderId(Long orderId);

    Optional<OrderDetail> findAllByBookAndOrder(Book book, Order order);

}
