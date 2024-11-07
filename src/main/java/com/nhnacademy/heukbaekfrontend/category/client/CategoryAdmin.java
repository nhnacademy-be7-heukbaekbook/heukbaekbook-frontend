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

@FeignClient(value = "categoryAdmin", url = "http://localhost:8082/api/admins/categories")
public interface CategoryAdmin {

    @GetMapping
    Page<CategoryDetailResponse> getCategories(Pageable pageable);

    @GetMapping("/{id}")
    CategoryDetailResponse getCategory(@PathVariable Long id);

    @PostMapping
    ResponseEntity<CategoryCreateResponse> createCategory(@RequestBody CategoryCreateRequest request);

    @PutMapping("/{id}")
    ResponseEntity<CategoryUpdateResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateRequest request);

    @DeleteMapping("/{id}")
    ResponseEntity<CategoryDeleteResponse> deleteCategory(@PathVariable Long id);
}
