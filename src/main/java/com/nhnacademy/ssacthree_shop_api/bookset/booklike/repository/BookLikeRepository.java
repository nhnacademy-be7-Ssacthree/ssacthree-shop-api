package com.nhnacademy.ssacthree_shop_api.bookset.booklike.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.booklike.domain.BookLike;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.domain.BookLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookLikeRepository extends JpaRepository<BookLike, BookLikeId> {
}
