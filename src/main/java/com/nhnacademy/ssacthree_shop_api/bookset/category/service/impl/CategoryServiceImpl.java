package com.nhnacademy.ssacthree_shop_api.bookset.category.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.exception.*;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.category.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private static final int MAX_DEPTH = 2; // 카테고리 최대 깊이


    /**
     * 새로운 카테고리를 저장합니다.
     *
     * @param categorySaveRequest 저장할 카테고리 정보
     * @return 저장된 카테고리 정보
     */
    @Override
    public CategoryInfoResponse saveCategory(CategorySaveRequest categorySaveRequest) {

        // CategorySaveRequest를 Category 엔티티로 변환
        Category category = new Category();
        category.setCategoryName(categorySaveRequest.getCategoryName());
        category.setCategoryIsUsed(true);

        // 상위 카테고리가 있는 경우
        if (categorySaveRequest.getSuperCategoryId() != null) {
            // 상위 카테고리 ID로 조회
            Category superCategory = categoryRepository.findById(categorySaveRequest.getSuperCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("상위 카테고리를 찾을 수 없습니다."));

            // 상위 카테고리가 사용중이지 않을 경우 상위 카테고리로 설정 불가능
            if (!superCategory.getCategoryIsUsed()) {
                throw new CategoryNotUsableException(superCategory.getCategoryId());
            }

            // 모든 상위 계층 카테고리 이름과 비교하여 중복 여부 확인
            checkNameConflictWithSuperCategories(superCategory, category.getCategoryName());

            // 같은 상위 카테고리 아래에서 이름 중복 확인
            boolean hasDuplicateName = categoryRepository.existsBySuperCategoryAndCategoryName(superCategory, category.getCategoryName());
            if (hasDuplicateName) {
                throw new DuplicateCategoryNameException("같은 상위 카테고리 아래에 같은 이름의 카테고리가 존재합니다.");
            }


            category.setSuperCategory(superCategory); // 상위 카테고리 설정
        } else {
            // 상위 카테고리가 없는 경우
            category.setSuperCategory(null);
        }

        // 카테고리 저장
        Category savedCategory = categoryRepository.save(category);

        return new CategoryInfoResponse(savedCategory);
    }

    /**
     * 상위 계층의 모든 카테고리 이름과 중복되는지 확인합니다.
     * - 재귀적으로 상위 계층을 탐색하며 저장하려는 카테고리 이름이 상위 카테고리와 같을 경우 예외를 발생시킵니다.
     *
     * @param category 상위 계층에서 이름을 비교할 카테고리 (현재 카테고리의 상위 카테고리)
     * @param name     저장하려는 카테고리 이름
     * @throws IllegalArgumentException 상위 계층 중 하나의 카테고리와 이름이 중복될 경우 예외 발생
     */
    private void checkNameConflictWithSuperCategories(Category category, String name) {
        if (category != null) {
            // 상위 카테고리 이름과 비교
            if (category.getCategoryName().equals(name)) {
                throw new DuplicateCategoryNameException("상위 계층의 카테고리와 같은 이름을 설정할 수 없습니다.");
            }
            // 재귀 호출로 더 상위 계층의 이름 확인
            checkNameConflictWithSuperCategories(category.getSuperCategory(), name);
        }
    }


    /**
     * 전체 카테고리 트리를 조회합니다.
     * 최상위 카테고리부터 시작하여 계층 구조로 조회할 수 있습니다.
     *
     * @return 카테고리 트리 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryInfoResponse> getAllCategories() {
        List<CategoryInfoResponse> categoryTree = new ArrayList<>();

        // 최상위 카테고리부터 시작하여 트리 구조로 추가
        List<Category> rootCategories = categoryRepository.findBySuperCategoryIsNull();

        for (Category rootCategory : rootCategories) {
            CategoryInfoResponse rootDto = new CategoryInfoResponse(rootCategory);
            loadChildCategories(rootDto, rootCategory); // 하위 카테고리를 계층 구조로 추가
            categoryTree.add(rootDto);
        }

        return categoryTree;
    }

    /**
     * 각 카테고리의 자식 카테고리를 계층적으로 불러와 설정하는 메서드
     *
     * @param parentDto      부모 카테고리 DTO
     * @param parentCategory 부모 카테고리
     */
    private void loadChildCategories(CategoryInfoResponse parentDto, Category parentCategory) {
        List<Category> children = categoryRepository.findBySuperCategory(parentCategory);
        List<CategoryInfoResponse> childrenDtos = new ArrayList<>();

        for (Category child : children) {
            CategoryInfoResponse childDto = new CategoryInfoResponse(child);
            loadChildCategories(childDto, child); // 자식의 자식들도 재귀적으로 설정. 자식이 없으면 빈 리스트. 재귀 종료됨.
            childrenDtos.add(childDto);
        }

        parentDto.setChildren(childrenDtos);

    }

    /**
     * 특정 ID를 가진 카테고리를 조회합니다.
     * 존재하지 않을 경우 예외가 발생합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 조회된 카테고리
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryInfoResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        return new CategoryInfoResponse(category);
    }

    /**
     * 주어진 부모 카테고리 ID에 대한 자식 카테고리를 조회합니다.
     *
     * @param parentCategoryId 부모 카테고리 ID
     * @return 자식 카테고리 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryInfoResponse> getChildCategories(Long parentCategoryId) {
        Category parent = categoryRepository.findById(parentCategoryId)
                .orElseThrow(() -> new CategoryNotFoundException(parentCategoryId));
        return categoryRepository.findBySuperCategory(parent).stream()
                .map(CategoryInfoResponse::new) // DTO 변환
                .collect(Collectors.toList());
    }

    /**
     * 최상위 카테고리를 조회합니다.
     * 부모 카테고리가 없는 최상위 카테고리들만 반환합니다.
     *
     * @return 최상위 카테고리 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryInfoResponse> getRootCategories() {
        return categoryRepository.findBySuperCategoryIsNull().stream()
                .map(CategoryInfoResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 이름을 기준으로 카테고리를 검색합니다.
     * 대소문자를 구분하지 않고 부분 일치 검색을 수행합니다.
     *
     * @param name 카테고리 이름
     * @return 검색된 카테고리 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryInfoResponse> searchCategoriesByName(String name) {
        List<Category> categories = categoryRepository.findCategoriesByName(name);
        if (categories.isEmpty()) {
            throw new CategoryNameNotFoundException(name);
        }
        return categories.stream()
                .map(CategoryInfoResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 카테고리의 최상위 카테고리까지의 경로를 조회합니다.
     * 주어진 카테고리부터 상위 카테고리를 따라 최상위까지 경로를 반환합니다.
     * 반환된 리스트에는 최상위 카테고리부터 최하위 카테고리 순으로 저장되어 있습니다.
     *
     * @param categoryId 카테고리 ID
     * @return 최상위 카테고리까지의 경로 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryInfoResponse> getCategoryPath(Long categoryId) {
        return categoryRepository.findCategoryPath(categoryId).stream()
                .map(CategoryInfoResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 카테고리의 지정 조회 깊이의 하위 카테고리 목록을 가져옵니다.
     *
     * @param categoryId 카테고리 ID
     * @param depth      조회 깊이 (1~MAX_DEPTH로 지정 가능)
     * @return 조회 깊이의 하위 카테고리 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryInfoResponse> getCategoryWithChildren(Long categoryId, int depth) {
        if (depth < 0 || depth > MAX_DEPTH) {
            throw new InvalidCategoryDepthException(depth, MAX_DEPTH);
        }

        Map<Integer, List<Category>> lowerCategories = new HashMap<>();
        Category rootCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        List<Category> currentLevelCategories = List.of(rootCategory);

        for (int i = 1; i <= depth; i++) {
            List<Category> nextLevelCategories = new ArrayList<>();
            for (Category category : currentLevelCategories) {
                List<Category> children = categoryRepository.findBySuperCategory(category);
                nextLevelCategories.addAll(children);
            }

            // 다음 레벨에 하위 카테고리가 없으면 예외를 발생시킴
            if (nextLevelCategories.isEmpty()) {
                throw new CategoryDepthNotFoundException(i); // i는 현재 깊이
            }

            lowerCategories.put(i, new ArrayList<>(nextLevelCategories));
            currentLevelCategories = nextLevelCategories;
        }

        return lowerCategories.get(depth).stream()
                .map(CategoryInfoResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 카테고리의 모든 하위 카테고리(자식 및 자손)를 조회합니다.
     * 재귀적으로 자식 카테고리를 조회하여 모든 하위 카테고리를 반환합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 모든 하위 카테고리 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryInfoResponse> getAllDescendants(Long categoryId) {
        return categoryRepository.findAllDescendants(categoryId).stream()
                .map(CategoryInfoResponse::new)
                .collect(Collectors.toList());
    }


    /**
     * 기존 카테고리를 업데이트합니다.
     *
     * @param categoryId 업데이트할 카테고리의 ID
     * @param request    request 업데이트할 카테고리 정보
     * @return 업데이트된 카테고리 정보
     */
    @Override
    public CategoryInfoResponse updateCategory(Long categoryId, CategoryUpdateRequest request) {

        // 업데이트할 카테고리 조회
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        if(!category.getCategoryIsUsed()){
            throw new CategoryNotFoundException(categoryId);
        }

        // 상위 카테고리 설정
        if (request.getSuperCategoryId() != null) {
            Category superCategory = categoryRepository.findById(request.getSuperCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("상위 카테고리를 찾을 수 없습니다."));

            // 상위 카테고리가 사용 중이지 않으면 설정 불가
            if (!superCategory.getCategoryIsUsed()) {
                throw new CategoryNotUsableException(superCategory.getCategoryId());
            }

            // 모든 상위 계층 카테고리 이름과 비교하여 중복 확인
            checkNameConflictWithSuperCategories(superCategory, request.getCategoryName());

            // 같은 상위 카테고리 아래에서 이름 중복 확인
            boolean hasDuplicateName = categoryRepository.existsBySuperCategoryAndCategoryName(superCategory, request.getCategoryName());
            if (hasDuplicateName && !category.getCategoryName().equals(request.getCategoryName())) {
                throw new DuplicateCategoryNameException("같은 상위 카테고리 아래에 같은 이름의 카테고리가 존재합니다.");
            }

            category.setSuperCategory(superCategory);
        } else {
            category.setSuperCategory(null);
        }

        // 자신의 하위 카테고리 아래에서 이름 중복 확인
        List<Category> childrenCategories = categoryRepository.findAllDescendants(categoryId);

        for (Category child : childrenCategories) {
            if (!category.getCategoryName().equals(request.getCategoryName()) && child.getCategoryName().equals(request.getCategoryName())) {
                throw new DuplicateCategoryNameException("하위 카테고리 중에 같은 이름의 카테고리가 존재합니다.");
            }
        }

        // 카테고리 이름과 사용 여부 업데이트
        category.setCategoryName(request.getCategoryName());

        // 카테고리 저장
        Category updatedCategory = categoryRepository.save(category);

        return new CategoryInfoResponse(updatedCategory); // 업데이트된 카테고리를 DTO로 반환
    }

    /**
     * 특정 카테고리를 소프트 삭제하여 사용 여부를 false로 변경합니다.
     * - 사용 중인 하위 카테고리가 있는 경우 상태 변경 불가
     *
     * @param categoryId 소프트 삭제할 카테고리 ID
     * @return 상태 변경이 성공하면 true, 사용 중인 하위 카테고리가 있어 실패하면 예외 발생
     */
    @Override
    public boolean deleteCategory(Long categoryId) {
        // 삭제할 카테고리 조회
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        // 사용 중인 하위 카테고리가 있는지 확인
        boolean hasUsedSubCategory = categoryRepository.findBySuperCategory(category).stream()
                .anyMatch(Category::getCategoryIsUsed);

        if (hasUsedSubCategory) {
            throw new SubCategoryInUseException(categoryId);
        }

        // 소프트 삭제로 사용 여부를 false로 변경
        category.setCategoryIsUsed(false);
        categoryRepository.save(category);

        return true;
    }


}
