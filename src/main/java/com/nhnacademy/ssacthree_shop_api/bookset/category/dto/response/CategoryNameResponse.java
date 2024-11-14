package com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryNameResponse {
    private Long categoryId;
    private String categoryName;

    public CategoryNameResponse(Category category){
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
    }

}
