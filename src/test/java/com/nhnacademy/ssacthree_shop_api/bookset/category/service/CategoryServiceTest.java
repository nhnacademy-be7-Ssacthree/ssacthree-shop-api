package com.nhnacademy.ssacthree_shop_api.bookset.category.service;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.exception.CategoryDepthNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.category.exception.CategoryNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.category.exception.DuplicateCategoryNameException;
import com.nhnacademy.ssacthree_shop_api.bookset.category.exception.SuperCategoryNotUsableException;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.category.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void saveCategory_rootCategory(){
        CategorySaveRequest request = new CategorySaveRequest("문학", null);
        Category savedCategory = new Category();
        savedCategory.setCategoryName("문학");
        savedCategory.setCategoryIsUsed(true);
        savedCategory.setSuperCategory(null);

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryInfoResponse response = categoryService.saveCategory(request);


        assertEquals("문학", response.getCategoryName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void saveCategory_subCategory() {

        CategorySaveRequest request = new CategorySaveRequest("국내 도서", null);
        Category savedCategory = new Category();
        savedCategory.setCategoryName("국내 도서");
        savedCategory.setCategoryIsUsed(true);


        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryInfoResponse response = categoryService.saveCategory(request);

        CategorySaveRequest request2 = new CategorySaveRequest("문학", savedCategory.getCategoryId());
        Category savedCategory2 = new Category();
        savedCategory2.setCategoryName("문학");
        savedCategory2.setCategoryIsUsed(true);
        savedCategory2.setSuperCategory(savedCategory);

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory2);

        CategoryInfoResponse response2 = categoryService.saveCategory(request2);


        assertEquals("문학", response2.getCategoryName());
        verify(categoryRepository, times(2)).save(any(Category.class));
    }

    @Test
    void saveCategory_ShouldThrowException_WhenSuperCategoryNotUsable() throws Exception {

        // 상위 카테고리
        Category superCategory = new Category();
        setCategoryId(superCategory, 1L);
        superCategory.setCategoryName("상위 카테고리");
        superCategory.setCategoryIsUsed(false); // 사용 불가능 상태로 설정

        when(categoryRepository.findById(superCategory.getCategoryId())).thenReturn(Optional.of(superCategory));

        // 상위 카테고리를 가진 CategorySaveRequest 생성
        CategorySaveRequest request = new CategorySaveRequest("새 카테고리", superCategory.getCategoryId());

        // saveCategory 호출 시 SuperCategoryNotUsableException이 발생하는지 검증
        assertThrows(SuperCategoryNotUsableException.class, () -> categoryService.saveCategory(request));
    }

    // 리플렉션을 통해 ID를 설정하는 메서드
    private void setCategoryId(Category category, Long id) throws Exception {
        Field field = Category.class.getDeclaredField("categoryId");
        field.setAccessible(true);
        field.set(category, id);
    }

    @Test
    void saveCategory_ShouldThrowException_WhenCategoryNameIsSameWithSuperCategory() throws Exception {
        // 상위 카테고리
        Category superCategory = new Category();
        setCategoryId(superCategory, 1L);
        superCategory.setCategoryName("문학");
        superCategory.setCategoryIsUsed(true);

        when(categoryRepository.findById(superCategory.getCategoryId())).thenReturn(Optional.of(superCategory));

        // 상위 카테고리를 가진 CategorySaveRequest 생성
        CategorySaveRequest request = new CategorySaveRequest("문학", superCategory.getCategoryId());

        // saveCategory 호출 시 DuplicateCategoryNameException이 발생하는지 검증
        assertThrows(DuplicateCategoryNameException.class, () -> categoryService.saveCategory(request));
    }

    @Test
    void saveCategory_ShouldThrowException_WhenCategoryNameIsSameWithSameLevelCategory() throws Exception {
        // 상위 카테고리
        Category superCategory = new Category();
        setCategoryId(superCategory, 1L);
        superCategory.setCategoryName("국내 도서");
        superCategory.setCategoryIsUsed(true);

        when(categoryRepository.findById(superCategory.getCategoryId())).thenReturn(Optional.of(superCategory));

        // 상위 카테고리 아래에 '문학'이라는 카테고리가 있음을 알림.
        when(categoryRepository.existsBySuperCategoryAndCategoryName(superCategory, "문학")).thenReturn(true);

        // 상위 카테고리(국내도서)를 가진 CategorySaveRequest 생성 (중복 이름 "문학")
        CategorySaveRequest request = new CategorySaveRequest("문학", superCategory.getCategoryId());

        // saveCategory 호출 시 DuplicateCategoryNameException이 발생하는지 검증
        assertThrows(DuplicateCategoryNameException.class, () -> categoryService.saveCategory(request));

    }


    @Test
    void getCategoryById() {
        Category category = new Category();
        category.setCategoryName("문학");
        category.setCategoryIsUsed(true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryInfoResponse response = categoryService.getCategoryById(1L);

        assertEquals("문학", response.getCategoryName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void getCategoryById_Exception() {
        lenient().when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(1L));
    }

    @Test
    void getChildCategories() {
        Category parent = new Category();
        parent.setCategoryName("문학");
        parent.setCategoryIsUsed(true);

        Category child = new Category();
        child.setCategoryName("소설");
        child.setCategoryIsUsed(true);
        child.setSuperCategory(parent);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(categoryRepository.findBySuperCategory(parent)).thenReturn(List.of(child));

        List<CategoryInfoResponse> children = categoryService.getChildCategories(1L);

        assertEquals(1, children.size());
        assertEquals("소설", children.get(0).getCategoryName());
    }

    @Test
    void deleteCategory() {
        Category category = new Category();
        category.setCategoryName("문학");
        category.setCategoryIsUsed(true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findBySuperCategory(category)).thenReturn(Collections.emptyList());

        boolean result = categoryService.deleteCategory(1L);

        assertTrue(result);
        verify(categoryRepository, times(1)).save(category);
        assertFalse(category.getCategoryIsUsed());
    }

    @Test
    void searchCategoriesByName() {
        Category category = new Category();
        category.setCategoryName("소설");
        category.setCategoryIsUsed(true);

        when(categoryRepository.findCategoriesByName("소설")).thenReturn(List.of(category));

        List<CategoryInfoResponse> results = categoryService.searchCategoriesByName("소설");

        assertEquals(1, results.size());
        assertEquals("소설", results.get(0).getCategoryName());
    }

    @Test
    void getAllCategories_ShouldReturnThreeLevelCategoryTree() {
        // 최상위 카테고리 생성
        Category rootCategory = new Category();
        rootCategory.setCategoryName("국내 도서");
        rootCategory.setCategoryIsUsed(true);

        // 중간 카테고리 생성
        Category midCategory = new Category();
        midCategory.setCategoryName("문학");
        midCategory.setCategoryIsUsed(true);
        midCategory.setSuperCategory(rootCategory);

        // 하위 카테고리 생성
        Category childCategory = new Category();
        childCategory.setCategoryName("시");
        childCategory.setCategoryIsUsed(true);
        childCategory.setSuperCategory(midCategory);

        // 각 계층에 대한 모킹 설정
        when(categoryRepository.findBySuperCategoryIsNull()).thenReturn(List.of(rootCategory));
        when(categoryRepository.findBySuperCategory(rootCategory)).thenReturn(List.of(midCategory));
        when(categoryRepository.findBySuperCategory(midCategory)).thenReturn(List.of(childCategory));
        when(categoryRepository.findBySuperCategory(childCategory)).thenReturn(Collections.emptyList());

        // 서비스 메서드 호출
        List<CategoryInfoResponse> categoryTree = categoryService.getAllCategories();

        // 검증
        assertEquals(1, categoryTree.size()); // 최상위 카테고리는 1개여야 함
        CategoryInfoResponse rootResponse = categoryTree.get(0);
        assertEquals("국내 도서", rootResponse.getCategoryName());

        // 중간 카테고리 검증
        assertEquals(1, rootResponse.getChildren().size());
        CategoryInfoResponse midResponse = rootResponse.getChildren().get(0);
        assertEquals("문학", midResponse.getCategoryName());

        // 하위 카테고리 검증
        assertEquals(1, midResponse.getChildren().size());
        CategoryInfoResponse childResponse = midResponse.getChildren().get(0);
        assertEquals("시", childResponse.getCategoryName());

        // 하위 카테고리는 자식이 없어야 함
        assertTrue(childResponse.getChildren().isEmpty());
    }


    @Test
    void getRootCategories() {
        Category rootCategory = new Category();
        rootCategory.setCategoryName("국내도서");
        rootCategory.setCategoryIsUsed(true);

        when(categoryRepository.findBySuperCategoryIsNull()).thenReturn(List.of(rootCategory));

        List<CategoryInfoResponse> rootCategories = categoryService.getRootCategories();

        assertEquals(1, rootCategories.size());
        assertEquals("국내도서", rootCategories.get(0).getCategoryName());
    }

    @Test
    void getCategoryPath() {
        Category rootCategory = new Category();
        rootCategory.setCategoryName("국내도서");
        rootCategory.setCategoryIsUsed(true);

        Category subCategory = new Category();
        subCategory.setCategoryName("문학");
        subCategory.setCategoryIsUsed(true);
        subCategory.setSuperCategory(rootCategory);

        when(categoryRepository.findCategoryPath(2L)).thenReturn(List.of(rootCategory, subCategory));

        List<CategoryInfoResponse> path = categoryService.getCategoryPath(2L);

        assertEquals(2, path.size());
        assertEquals("국내도서", path.get(0).getCategoryName());
        assertEquals("문학", path.get(1).getCategoryName());
    }

    @Test
    void getCategoryWithChildren() {
        Category parentCategory = new Category();
        parentCategory.setCategoryName("문학");
        parentCategory.setCategoryIsUsed(true);

        Category childCategory = new Category();
        childCategory.setCategoryName("소설");
        childCategory.setCategoryIsUsed(true);
        childCategory.setSuperCategory(parentCategory);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findBySuperCategory(parentCategory)).thenReturn(List.of(childCategory));

        List<CategoryInfoResponse> children = categoryService.getCategoryWithChildren(1L, 1);

        assertEquals(1, children.size());
        assertEquals("소설", children.get(0).getCategoryName());
    }

    @Test
    @DisplayName("깊이 설정이 유효하지 않은 경우 에러") //todo: test code 모두 displayName 쓰기
    void getCategoryWithChildren_ShouldThrowException_DepthInvalid() {
        Category parentCategory = new Category();
        parentCategory.setCategoryName("문학");
        parentCategory.setCategoryIsUsed(true);

        Category childCategory = new Category();
        childCategory.setCategoryName("소설");
        childCategory.setCategoryIsUsed(true);
        childCategory.setSuperCategory(parentCategory);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findBySuperCategory(parentCategory)).thenReturn(List.of(childCategory));

        assertThrows(CategoryDepthNotFoundException.class, () -> categoryService.getCategoryWithChildren(1L, 2));
    }

    @Test
    void getAllDescendants() {
        Category rootCategory = new Category();
        rootCategory.setCategoryName("국내도서");
        rootCategory.setCategoryIsUsed(true);

        Category childCategory = new Category();
        childCategory.setCategoryName("문학");
        childCategory.setCategoryIsUsed(true);
        childCategory.setSuperCategory(rootCategory);

        Category grandChildCategory = new Category();
        grandChildCategory.setCategoryName("소설");
        grandChildCategory.setCategoryIsUsed(true);
        grandChildCategory.setSuperCategory(childCategory);

        when(categoryRepository.findAllDescendants(1L)).thenReturn(List.of(childCategory, grandChildCategory));

        List<CategoryInfoResponse> descendants = categoryService.getAllDescendants(1L);

        assertEquals(2, descendants.size());
        assertEquals("문학", descendants.get(0).getCategoryName());
        assertEquals("소설", descendants.get(1).getCategoryName());
    }

    @Test
    void updateCategory_ShouldUpdateCategorySuccessfully() throws Exception {
        // 기존 카테고리 및 상위 카테고리 설정
        Category category = new Category();
        setCategoryId(category, 1L);
        category.setCategoryName("기존 카테고리");
        category.setCategoryIsUsed(true);

        Category superCategory = new Category();
        setCategoryId(superCategory, 2L);
        superCategory.setCategoryName("상위 카테고리");
        superCategory.setCategoryIsUsed(true);

        // Mock 설정
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category), Optional.of(superCategory));
        when(categoryRepository.existsBySuperCategoryAndCategoryName(superCategory, "새로운 이름")).thenReturn(false);
        when(categoryRepository.findAllDescendants(anyLong())).thenReturn(Collections.emptyList());
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            // 상위 카테고리가 설정되었는지 확인
            Category savedCategory = invocation.getArgument(0);
            assertEquals(superCategory, savedCategory.getSuperCategory()); // 상위 카테고리가 제대로 설정되었는지 확인
            return savedCategory;
        });

        // 업데이트 요청 생성 및 호출
        CategoryUpdateRequest request = new CategoryUpdateRequest("새로운 이름", superCategory.getCategoryId());
        CategoryInfoResponse response = categoryService.updateCategory(category.getCategoryId(), request);

        // 결과 검증
        assertEquals("새로운 이름", response.getCategoryName());
        //assertTrue(response.isCategoryIsUsed()); // 사용 여부가 유지되는지 확인
    }

    @Test
    void updateCategory_ShouldThrowException_WhenDuplicateCategoryNameExistsInChildCategories() {
        // 기존 카테고리 및 하위 카테고리 설정
        Category category = new Category();
        category.setCategoryName("기존 카테고리");

        Category childCategory = new Category();
        childCategory.setCategoryName("중복된 이름");

        // Mock 설정
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findAllDescendants(1L)).thenReturn(List.of(childCategory));

        // 요청 생성 및 예외 검증
        CategoryUpdateRequest request = new CategoryUpdateRequest("중복된 이름", null);
        assertThrows(DuplicateCategoryNameException.class, () -> categoryService.updateCategory(1L, request));
    }
}


