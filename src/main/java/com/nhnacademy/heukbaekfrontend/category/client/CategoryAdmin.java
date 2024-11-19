package com.nhnacademy.heukbaekfrontend.category.client;

import com.nhnacademy.heukbaekfrontend.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.heukbaekfrontend.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryCreateResponse;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryDeleteResponse;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryDetailResponse;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryUpdateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "categoryAdmin", url = "http://localhost:8082")
public interface CategoryAdmin {

    @GetMapping("/api/admin/categories")
    Page<CategoryDetailResponse> getCategories(Pageable pageable);

    @GetMapping("/api/admin/categories/{id}")
    CategoryDetailResponse getCategory(@PathVariable Long id);

    @PostMapping("/api/admin/categories")
    ResponseEntity<CategoryCreateResponse> createCategory(@RequestBody CategoryCreateRequest request);

    @PutMapping("/api/admin/categories/{id}")
    ResponseEntity<CategoryUpdateResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateRequest request);

    @DeleteMapping("/api/admin/categories/{id}")
    ResponseEntity<CategoryDeleteResponse> deleteCategory(@PathVariable Long id);

    @GetMapping("/api/admin/categories/list")
    ResponseEntity<List<String>> getCategoryPaths();

}
