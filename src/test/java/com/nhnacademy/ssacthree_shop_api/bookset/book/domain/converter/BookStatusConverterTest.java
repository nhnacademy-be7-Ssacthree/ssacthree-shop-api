package com.nhnacademy.ssacthree_shop_api.bookset.book.domain.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import org.junit.jupiter.api.Test;

class BookStatusConverterTest {

    private final BookStatusConverter converter = new BookStatusConverter();

    @Test
    void convertToDatabaseColumn_withValidBookStatus() {
        // Given
        BookStatus bookStatus = BookStatus.ON_SALE;

        // When
        String dbValue = converter.convertToDatabaseColumn(bookStatus);

        // Then
        assertEquals("판매 중", dbValue);
    }

    @Test
    void convertToDatabaseColumn_withNull() {
        // When
        String dbValue = converter.convertToDatabaseColumn(null);

        // Then
        assertNull(dbValue);
    }

    @Test
    void convertToEntityAttribute_withValidDbValue() {
        // Given
        String dbValue = "판매 중";

        // When
        BookStatus bookStatus = converter.convertToEntityAttribute(dbValue);

        // Then
        assertEquals(BookStatus.ON_SALE, bookStatus);
    }

    @Test
    void convertToEntityAttribute_withInvalidDbValue() {
        // Given
        String dbValue = "존재하지 않는 상태";

        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> converter.convertToEntityAttribute(dbValue));
    }

    @Test
    void convertToEntityAttribute_withNull() {
        // When
        BookStatus bookStatus = converter.convertToEntityAttribute(null);

        // Then
        assertNull(bookStatus);
    }
}
