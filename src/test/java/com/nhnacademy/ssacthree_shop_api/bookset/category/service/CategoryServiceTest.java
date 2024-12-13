package com.nhnacademy.ssacthree_shop_api.bookset.category.service;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.exception.CategoryDepthNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.category.exception.CategoryNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.category.exception.DuplicateCategoryNameException;
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
class CategoryServiceTest {

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
    void saveCategory_ShouldThrowException_WhenCategoryNameIsSameWithSuperCategory() throws Exception {
        // Arrange
        // 상위 카테고리 생성
        Category superCategory = new Category();
        setCategoryId(superCategory, 1L);
        superCategory.setCategoryName("문학");
        superCategory.setCategoryIsUsed(true);

        // Mock 설정
        when(categoryRepository.findByCategoryIdAndCategoryIsUsedTrue(superCategory.getCategoryId()))
                .thenReturn(superCategory);

        // 요청 생성
        CategorySaveRequest request = new CategorySaveRequest("문학", superCategory.getCategoryId());

        // Act & Assert
        assertThrows(DuplicateCategoryNameException.class, () -> categoryService.saveCategory(request));
    }

    @Test
    void saveCategory_ShouldThrowException_WhenCategoryNameIsSameWithSameLevelCategory() throws Exception {
        // Arrange
        // 상위 카테고리 생성
        Category superCategory = new Category();
        setCategoryId(superCategory, 1L);
        superCategory.setCategoryName("국내 도서");
        superCategory.setCategoryIsUsed(true);

        // 동일한 이름의 카테고리 생성
        Category duplicateCategory = new Category();
        setCategoryId(duplicateCategory, 2L);
        duplicateCategory.setCategoryName("문학");
        duplicateCategory.setSuperCategory(superCategory);
        duplicateCategory.setCategoryIsUsed(true);

        // Mock 설정
        when(categoryRepository.findByCategoryIdAndCategoryIsUsedTrue(superCategory.getCategoryId()))
                .thenReturn(superCategory);

        when(categoryRepository.findBySuperCategoryAndCategoryNameAndCategoryIsUsedTrue(superCategory, "문학"))
                .thenReturn(duplicateCategory);

        // 요청 생성
        CategorySaveRequest request = new CategorySaveRequest("문학", superCategory.getCategoryId());

        // Act & Assert
        assertThrows(DuplicateCategoryNameException.class, () -> categoryService.saveCategory(request));
    }

    // 리플렉션을 통해 ID를 설정하는 메서드
    private void setCategoryId(Category category, Long id) throws Exception {
        Field field = Category.class.getDeclaredField("categoryId");
        field.setAccessible(true);
        field.set(category, id);
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
        when(categoryRepository.findBySuperCategoryAndCategoryIsUsedTrue(parent)).thenReturn(List.of(child));

        List<CategoryInfoResponse> children = categoryService.getChildCategories(1L);

        assertEquals(1, children.size());
        assertEquals("소설", children.get(0).getCategoryName());
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
        when(categoryRepository.findBySuperCategoryIsNullAndCategoryIsUsed()).thenReturn(List.of(rootCategory));
        when(categoryRepository.findBySuperCategoryAndCategoryIsUsedTrue(rootCategory)).thenReturn(List.of(midCategory));
        when(categoryRepository.findBySuperCategoryAndCategoryIsUsedTrue(midCategory)).thenReturn(List.of(childCategory));
        when(categoryRepository.findBySuperCategoryAndCategoryIsUsedTrue(childCategory)).thenReturn(Collections.emptyList());

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

        when(categoryRepository.findBySuperCategoryIsNullAndCategoryIsUsed()).thenReturn(List.of(rootCategory));

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
        when(categoryRepository.findBySuperCategoryAndCategoryIsUsedTrue(parentCategory)).thenReturn(List.of(childCategory));

        List<CategoryInfoResponse> children = categoryService.getCategoryWithChildren(1L, 1);

        assertEquals(1, children.size());
        assertEquals("소설", children.get(0).getCategoryName());
    }

    @Test
    @DisplayName("깊이 설정이 유효하지 않은 경우 에러")
    void getCategoryWithChildren_ShouldThrowException_DepthInvalid() {
        Category parentCategory = new Category();
        parentCategory.setCategoryName("문학");
        parentCategory.setCategoryIsUsed(true);

        Category childCategory = new Category();
        childCategory.setCategoryName("소설");
        childCategory.setCategoryIsUsed(true);
        childCategory.setSuperCategory(parentCategory);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findBySuperCategoryAndCategoryIsUsedTrue(parentCategory)).thenReturn(List.of(childCategory));

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
    void updateCategory_ShouldThrowException_WhenDuplicateCategoryNameExistsInChildCategories() throws Exception {
        // Arrange
        Category category = new Category();
        setCategoryId(category, 1L);
        category.setCategoryName("기존 카테고리");
        category.setCategoryIsUsed(true);

        Category childCategory = new Category();
        setCategoryId(childCategory, 2L);
        childCategory.setCategoryName("중복된 이름");
        childCategory.setCategoryIsUsed(true);
        childCategory.setSuperCategory(category);

        // Mock 설정
        when(categoryRepository.findByCategoryIdAndCategoryIsUsedTrue(1L))
                .thenReturn(category);

        when(categoryRepository.findAllDescendants(1L))
                .thenReturn(List.of(childCategory));

        // 요청 생성
        CategoryUpdateRequest request = new CategoryUpdateRequest("중복된 이름", null);

        // Act & Assert
        assertThrows(DuplicateCategoryNameException.class, () -> categoryService.updateCategory(1L, request));
    }

    @Test
    void deleteCategory() throws Exception {
        // Arrange
        Category category = new Category();
        setCategoryId(category, 1L);
        category.setCategoryName("문학");
        category.setCategoryIsUsed(true);

        // Mock 설정
        when(categoryRepository.findByCategoryIdAndCategoryIsUsedTrue(1L))
                .thenReturn(category);

        when(categoryRepository.findBySuperCategoryAndCategoryIsUsedTrue(category))
                .thenReturn(Collections.emptyList());

        // Act
        boolean result = categoryService.deleteCategory(1L);

        // Assert
        assertTrue(result);
        verify(categoryRepository, times(1)).save(category);
        assertFalse(category.getCategoryIsUsed());
    }


}


