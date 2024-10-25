package com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카테고리 기본 정보 반환
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryInfoResponse {

    private String categoryName;

    private boolean categoryIsUsed;

    public CategoryInfoResponse(Category category) {
        this.categoryName = category.getCategoryName();
        this.categoryIsUsed = category.getCategoryIsUsed();
    }

}
