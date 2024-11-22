package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.domain.repo;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.domain.domain.OrderDetail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {

    Optional<OrderDetail> findAllByBookAndOrder(Book book, Order order);
}
