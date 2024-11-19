package com.nhnacademy.heukbaekfrontend.bookCategory.service;

import com.nhnacademy.heukbaekfrontend.category.dto.response.CategorySummaryResponse;

import java.util.List;

public interface BookCategoryService {

    List<CategorySummaryResponse> getBookCategoriesByBookId(Long bookId);
}
