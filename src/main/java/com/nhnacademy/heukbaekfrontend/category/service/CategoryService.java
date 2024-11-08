package com.nhnacademy.heukbaekfrontend.category.service;

import com.nhnacademy.heukbaekfrontend.category.client.CategoryAdmin;
import com.nhnacademy.heukbaekfrontend.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.heukbaekfrontend.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryCreateResponse;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryDeleteResponse;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryDetailResponse;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryUpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryAdmin categoryAdmin;

    @Autowired
    private CategoryService(CategoryAdmin categoryAdmin) {
        this.categoryAdmin = categoryAdmin;
    }

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
}
