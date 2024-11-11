package com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.dto;

import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookCategoryDto {
    private Long bookId;
    private CategoryNameResponse categoryNameResponse;
}
