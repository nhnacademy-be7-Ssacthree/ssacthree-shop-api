package com.nhnacademy.ssacthree_shop_api.bookset.book.mapper;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.BookDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDto convertToBookDto(Book book);

    Book convertToBook(BookDto bookDto);

}
