package com.nhnacademy.heukbaekfrontend.category.service;

import com.nhnacademy.heukbaekfrontend.category.client.CategoryAdmin;
import com.nhnacademy.heukbaekfrontend.category.client.CategoryClient;
import com.nhnacademy.heukbaekfrontend.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.heukbaekfrontend.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.heukbaekfrontend.category.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryAdmin categoryAdmin;
    private final RedisCategoryService redisCategoryService;

    public Page<CategoryDetailResponse> getAllCategories(Pageable pageable) {
        return categoryAdmin.getCategories(pageable);
    }

    public CategoryDetailResponse getCategoryById(Long id) {
        return categoryAdmin.getCategory(id);
    }

    public ResponseEntity<CategoryCreateResponse> registerCategory(CategoryCreateRequest request) {
        return categoryAdmin.createCategory(request);
    }

    public ResponseEntity<CategoryUpdateResponse> updateCategory(Long id, CategoryUpdateRequest request) {
        return categoryAdmin.updateCategory(id, request);
    }

    public ResponseEntity<CategoryDeleteResponse> deleteBook(Long categoryId) {
        return categoryAdmin.deleteCategory(categoryId);
    }

    public List<CategorySummaryResponse> getTopCategories() {
//        return categoryClient.getTopCategories();
        return redisCategoryService.getCategories();
    }

    public List<String> getCategoryPaths() {
        return categoryAdmin.getCategoryPaths().getBody();
    }

}
