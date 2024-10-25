package com.nhnacademy.ssacthree_shop_api.bookset.category.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController( CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 카테고리 저장하는 컨트롤러
     * @param request 카테고리 저장 정보
     * //@return
     */
    @PostMapping
    public ResponseEntity<CategoryInfoResponse> createCategory(@RequestBody CategorySaveRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON)
                .body(categoryService.saveCategory(request));
    }
}
