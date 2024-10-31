package com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 카테고리 저장 DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategorySaveRequest {
    @NotBlank
    @Size(min = 1, max = 20, message = "카테고리 이름은 1~20자로 설정해야 합니다.")
    private String categoryName;

    private Category superCategory;

    private boolean categoryIsUsed;


}
