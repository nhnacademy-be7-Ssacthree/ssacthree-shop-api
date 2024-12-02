package com.nhnacademy.ssacthree_shop_api.bookset.book.domain.converter;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import jakarta.persistence.AttributeConverter;


public class BookStatusConverter implements AttributeConverter<BookStatus, String> {

    /**
     * 데이터베이스에 요청을 보낼 때 BookStatus.ON_SALE -> "판매 중"
     */
    @Override
    public String convertToDatabaseColumn(BookStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getStatus();
    }

    /**
     * 데이터베이스에서 값을 읽어올 때 "판매 중" -> BookStatus.ON_SALE
     */
    @Override
    public BookStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return BookStatus.getBookStatus(dbData);
    }

}
