package com.nhnacademy.ssacthree_shop_api.bookset.category.service;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.exception.CategoryNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.category.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void getAllCategories() {
        Category category1 = new Category();
        category1.setCategoryName("국내도서");
        category1.setCategoryIsUsed(true);

        Category category2 = new Category();
        category2.setCategoryName("해외도서");
        category2.setCategoryIsUsed(true);

        when(categoryRepository.findBySuperCategoryIsNull()).thenReturn(List.of(category1, category2));

        List<CategoryInfoResponse> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
        verify(categoryRepository, times(1)).findBySuperCategoryIsNull();
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
}
