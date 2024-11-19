package com.nhnacademy.heukbaekfrontend.bookCategory.service.client;

import com.nhnacademy.heukbaekfrontend.category.dto.response.CategorySummaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "bookCategoryClient", url = "http://localhost:8082/api")
public interface BookCategoryClient {

    @GetMapping("/books/{bookId}/book-categories")
    List<CategorySummaryResponse> getBookCategoriesByBookId(@PathVariable Long bookId);
}
