package com.nhnacademy.heukbaekfrontend.category.client;

import com.nhnacademy.heukbaekfrontend.category.dto.response.CategorySummaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "categoryClient", url = "http://localhost:8082/api/categories")
public interface CategoryClient {

    @GetMapping
    List<CategorySummaryResponse> getTopCategories();
}
