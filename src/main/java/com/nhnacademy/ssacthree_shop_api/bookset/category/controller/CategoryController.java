package com.nhnacademy.ssacthree_shop_api.bookset.category.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shop")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController( CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 새로운 카테고리 생성
     * @param request 카테고리 생성 요청 데이터
     * @return 생성된 카테고리 정보
     */
    @PostMapping("/admin/category")
    public ResponseEntity<CategoryInfoResponse> createCategory(@RequestBody CategorySaveRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON)
                .body(categoryService.saveCategory(request));
    }

    /**
     * 전체 카테고리 트리 조회
     *
     * @return 전체 카테고리 트리 정보
     */
    @GetMapping("/category")
    public ResponseEntity<List<CategoryInfoResponse>> getAllCategories() {
        List<CategoryInfoResponse> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /**
     * 특정 ID를 가진 카테고리 조회
     *
     * @param categoryId 카테고리 ID
     * @return 조회된 카테고리 정보
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<CategoryInfoResponse> getCategoryById(@PathVariable Long categoryId) {
        CategoryInfoResponse category = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    /**
     * 주어진 부모 카테고리의 자식 카테고리 목록 조회
     *
     * @param parentCategoryId 부모 카테고리 ID
     * @return 자식 카테고리 목록
     */
    @GetMapping("/category/{parentCategoryId}/children")
    public ResponseEntity<List<CategoryInfoResponse>> getChildCategories(@PathVariable Long parentCategoryId) {
        List<CategoryInfoResponse> children = categoryService.getChildCategories(parentCategoryId);
        return new ResponseEntity<>(children, HttpStatus.OK);
    }

    /**
     * 최상위 카테고리 목록 조회
     *
     * @return 최상위 카테고리 목록
     */
    @GetMapping("/category/root")
    public ResponseEntity<List<CategoryInfoResponse>> getRootCategories() {
        List<CategoryInfoResponse> rootCategories = categoryService.getRootCategories();
        return new ResponseEntity<>(rootCategories, HttpStatus.OK);
    }

    /**
     * 카테고리 이름으로 검색
     *
     * @param name 검색할 카테고리 이름
     * @return 검색된 카테고리 목록
     */
    @GetMapping("/category/search")
    public ResponseEntity<List<CategoryInfoResponse>> searchCategoriesByName(@RequestParam String name) {
        List<CategoryInfoResponse> categories = categoryService.searchCategoriesByName(name);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /**
     * 특정 카테고리의 최상위 카테고리까지의 경로 조회
     *
     * @param categoryId 카테고리 ID
     * @return 최상위 카테고리까지의 경로
     */
    @GetMapping("/category/{categoryId}/path")
    public ResponseEntity<List<CategoryInfoResponse>> getCategoryPath(@PathVariable Long categoryId) {
        List<CategoryInfoResponse> path = categoryService.getCategoryPath(categoryId);
        return new ResponseEntity<>(path, HttpStatus.OK);
    }

    /**
     * 특정 카테고리 업데이트
     *
     * @param categoryId 업데이트할 카테고리 ID
     * @param request 업데이트 요청 데이터
     * @return 업데이트된 카테고리 정보
     */
    @PutMapping("/admin/category/{categoryId}")
    public ResponseEntity<CategoryInfoResponse> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryUpdateRequest request) {
        CategoryInfoResponse updatedCategory = categoryService.updateCategory(categoryId, request);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    /**
     * 특정 카테고리 소프트 삭제
     *
     * @param categoryId 소프트 삭제할 카테고리 ID
     * @return 삭제 상태 변경 결과
     */
    @DeleteMapping("/admin/category/{categoryId}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable Long categoryId) {
        boolean result = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 특정 카테고리의 지정 깊이의 하위 카테고리 조회
     *
     * @param categoryId 조회할 카테고리 ID
     * @param depth      조회 깊이
     * @return 지정 깊이의 하위 카테고리 목록
     */
    @GetMapping("/category/{categoryId}/children/depth/{depth}")
    public ResponseEntity<List<CategoryInfoResponse>> getCategoryWithChildren(
            @PathVariable long categoryId, @PathVariable int depth) {
        List<CategoryInfoResponse> categories = categoryService.getCategoryWithChildren(categoryId, depth);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(categories);
    }

    /**
     * 특정 카테고리의 모든 하위 카테고리 조회
     *
     * @param categoryId 조회할 카테고리 ID
     * @return 모든 하위 카테고리 목록
     */
    @GetMapping("/category/{categoryId}/descendants")
    public ResponseEntity<List<CategoryInfoResponse>> getAllDescendants(@PathVariable long categoryId) {
        List<CategoryInfoResponse> descendants = categoryService.getAllDescendants(categoryId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(descendants);
    }
}
