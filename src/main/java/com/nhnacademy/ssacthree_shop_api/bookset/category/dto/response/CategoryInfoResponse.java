package com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 카테고리 기본 정보 반환
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryInfoResponse {

    private Long categoryId;

    private String categoryName;

    private boolean categoryIsUsed;

    // 하위 카테고리들을 담을 리스트 (계층 구조를 위한 필드)
    @Setter
    private List<CategoryInfoResponse> children = new ArrayList<>();


    public CategoryInfoResponse(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.categoryIsUsed = category.getCategoryIsUsed();
    }

}
