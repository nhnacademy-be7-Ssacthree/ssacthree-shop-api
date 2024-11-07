package com.nhnacademy.ssacthree_shop_api.bookset.booktag.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.QBook;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.QBookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.repository.BookTagCustomRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.QTag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookTagCustomRepositoryImpl implements BookTagCustomRepository {
    private final JPAQueryFactory queryFactory;

    private static final QBookTag bookTag = QBookTag.bookTag;
    private static final QBook book = QBook.book;
    private static final QTag tag = QTag.tag;

    @Override
    public List<Tag> findBookTagsByBookId(Long bookId) {
        return queryFactory.select(tag)
                .from(bookTag)
                .where(bookTag.book.bookId.eq(bookId))
                .fetch();
    }
}
