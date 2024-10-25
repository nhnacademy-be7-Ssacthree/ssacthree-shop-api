package com.nhnacademy.ssacthree_shop_api.bookset.category.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.exception.CategoryNotExistsException;
import com.nhnacademy.ssacthree_shop_api.bookset.category.exception.SuperCategoryNotUsableException;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.category.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    @Override
    public CategoryInfoResponse saveCategory(CategorySaveRequest categorySaveRequest) {


        // CategorySaveRequest를 Category 엔티티로 변환
        Category category = new Category();
        category.setCategoryName(categorySaveRequest.getCategoryName());
        category.setCategoryIsUsed(categorySaveRequest.isCategoryIsUsed());

        // 상위 카테고리가 있는 경우
        if(categorySaveRequest.getSuperCategory() != null ) {
            // 상위 카테고리 ID로 조회
            Category superCategory = categoryRepository.findById(categorySaveRequest.getSuperCategory().getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("상위 카테고리를 찾을 수 없습니다."));
            // 상위 카테고리가 사용중이지 않을 경우 상위 카테고리로 설정 불가능
            if(!superCategory.getCategoryIsUsed()){
                throw new SuperCategoryNotUsableException(superCategory.getCategoryId());
            }
            category.setSuperCategory(superCategory); // 상위 카테고리 설정
        } else{
            // 상위 카테고리가 없는 경우
            category.setSuperCategory(null);
        }

        // 카테고리 저장
        Category savedCategory = categoryRepository.save(category);

        return new CategoryInfoResponse(category);
    }

    @Override
    public CategoryInfoResponse updateCategory(long categoryId, CategorySaveRequest categorySaveRequest) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotExistsException(categoryId));


        category.setCategoryName(categorySaveRequest.getCategoryName());
        category.setCategoryIsUsed(categorySaveRequest.isCategoryIsUsed());
        category.setSuperCategory(categorySaveRequest.getSuperCategory());

        // todo: 카테고리 위치/사용여부를 바꾸고 싶다면? -> 하위 카테고리가 있다면 위치/사용여부를 못 바꾸게 한다?
        // 같은 상위 카테고리 바로 아래에 속해있는 카테고리끼리는 이름이 같으면 안되고, 자신이 속해있는 상위카테고리와 이름이 같으면 안된다.
        categoryRepository.save(category);

        // todo: 질문
        return new CategoryInfoResponse(category);
    }

    @Override
    public boolean deleteCategory(long CategoryId) {
        Category category = categoryRepository.findById(CategoryId).orElseThrow(() -> new CategoryNotExistsException(CategoryId));

        return false;
    }

    // 최상위 카테고리 검색
    // 상위 카테고리 바로 아래 카테고리 검색
    // 하위 카테고리 바로 위 카테고리 검색
    // 하위 카테고리 최상 위 카테고리 검색
    // 하위 카테고리의 모든 상위 카테고리 검색(부모, 조상)
    // 최상위 카테고리에 속한 모든 카테고리 검색
    // 상위 카테고리의 하위 카테고리 트리 검색
    // 최상위 카테고리의 최하위 카테고리 검색 -> 국내 도서에 쿠폰을 적용할 때 '국내>문학>소설'일 경우 국내에 소속된 소설을 찾기 위한
    // 전체 카테고리 트리 검색


}
