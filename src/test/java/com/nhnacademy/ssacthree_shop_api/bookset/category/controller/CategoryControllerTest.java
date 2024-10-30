package com.nhnacademy.ssacthree_shop_api.bookset.category.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.service.CategoryService;
import com.nhnacademy.ssacthree_shop_api.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@Import(SecurityConfig.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    void createCategory() throws Exception {
        CategorySaveRequest request = new CategorySaveRequest("문학", null, true);
        CategoryInfoResponse response = new CategoryInfoResponse("문학", true, new ArrayList<>());

        when(categoryService.saveCategory(any(CategorySaveRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryName\":\"문학\",\"categoryIsUsed\":true}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryName").value("문학"));
    }

    @Test
    void getAllCategories() throws Exception {
        CategoryInfoResponse category1 = new CategoryInfoResponse("국내도서", true, new ArrayList<>());
        CategoryInfoResponse category2 = new CategoryInfoResponse("해외도서", true, new ArrayList<>());

        when(categoryService.getAllCategories()).thenReturn(List.of(category1, category2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].categoryName").value("국내도서"))
                .andExpect(jsonPath("$[1].categoryName").value("해외도서"));
    }

    @Test
    void getCategoryById() throws Exception {
        CategoryInfoResponse category = new CategoryInfoResponse("문학", true, new ArrayList<>());

        when(categoryService.getCategoryById(anyLong())).thenReturn(category);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryName").value("문학"));
    }

    @Test
    void getRootCategories() throws Exception {
        CategoryInfoResponse rootCategory = new CategoryInfoResponse("국내도서", true, new ArrayList<>());

        when(categoryService.getRootCategories()).thenReturn(List.of(rootCategory));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/root")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].categoryName").value("국내도서"));
    }

    @Test
    void getChildCategories() throws Exception {
        // 자식 카테고리 정보 설정
        CategoryInfoResponse childCategory1 = new CategoryInfoResponse("자식카테고리1", true, new ArrayList<>());
        CategoryInfoResponse childCategory2 = new CategoryInfoResponse("자식카테고리2", true, new ArrayList<>());

        // Mock 설정
        when(categoryService.getChildCategories(anyLong())).thenReturn(List.of(childCategory1, childCategory2));

        // MockMvc를 사용하여 요청 실행 및 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/1/children")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].categoryName").value("자식카테고리1"))
                .andExpect(jsonPath("$[1].categoryName").value("자식카테고리2"));
    }


    @Test
    void searchCategoriesByName() throws Exception {
        CategoryInfoResponse category = new CategoryInfoResponse("소설", true, new ArrayList<>());

        when(categoryService.searchCategoriesByName(anyString())).thenReturn(List.of(category));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/search")
                        .param("name", "소설")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].categoryName").value("소설"));
    }


    @Test
    void updateCategory() throws Exception {
        CategoryUpdateRequest updateRequest = new CategoryUpdateRequest("문학", null, true);
        CategoryInfoResponse response = new CategoryInfoResponse("문학", true, new ArrayList<>());

        when(categoryService.updateCategory(anyLong(), any(CategoryUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryName\":\"문학\",\"categoryIsUsed\":true}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryName").value("문학"));
    }

    @Test
    void deleteCategory() throws Exception {
        when(categoryService.deleteCategory(anyLong())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/category/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));
    }

    @Test
    void getCategoryWithChildren() throws Exception {
        // 특정 깊이의 하위 카테고리 설정
        CategoryInfoResponse childCategory1 = new CategoryInfoResponse("자식카테고리1", true, new ArrayList<>());
        CategoryInfoResponse childCategory2 = new CategoryInfoResponse("자식카테고리2", true, new ArrayList<>());

        // Mock 설정
        when(categoryService.getCategoryWithChildren(anyLong(), anyInt())).thenReturn(List.of(childCategory1, childCategory2));

        // MockMvc를 사용하여 요청 실행 및 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/1/children/depth/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].categoryName").value("자식카테고리1"))
                .andExpect(jsonPath("$[1].categoryName").value("자식카테고리2"));
    }

    @Test
    void getAllDescendants() throws Exception {
        // 모든 하위 카테고리 설정
        CategoryInfoResponse descendant1 = new CategoryInfoResponse("하위카테고리1", true, new ArrayList<>());
        CategoryInfoResponse descendant2 = new CategoryInfoResponse("하위카테고리2", true, new ArrayList<>());

        // Mock 설정
        when(categoryService.getAllDescendants(anyLong())).thenReturn(List.of(descendant1, descendant2));

        // MockMvc를 사용하여 요청 실행 및 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/1/descendants")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].categoryName").value("하위카테고리1"))
                .andExpect(jsonPath("$[1].categoryName").value("하위카테고리2"));
    }

    @Test
    void getCategoryPath() throws Exception {
        // 최상위 카테고리까지의 경로 설정
        CategoryInfoResponse rootCategory = new CategoryInfoResponse("루트카테고리", true, new ArrayList<>());
        CategoryInfoResponse middleCategory = new CategoryInfoResponse("중간카테고리", true, new ArrayList<>());
        CategoryInfoResponse targetCategory = new CategoryInfoResponse("대상카테고리", true, new ArrayList<>());

        // Mock 설정
        when(categoryService.getCategoryPath(anyLong())).thenReturn(List.of(rootCategory, middleCategory, targetCategory));

        // MockMvc를 사용하여 요청 실행 및 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/1/path")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].categoryName").value("루트카테고리"))
                .andExpect(jsonPath("$[1].categoryName").value("중간카테고리"))
                .andExpect(jsonPath("$[2].categoryName").value("대상카테고리"));
    }


}
