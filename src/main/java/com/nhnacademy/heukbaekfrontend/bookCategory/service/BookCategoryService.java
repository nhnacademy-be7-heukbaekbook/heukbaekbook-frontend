package com.nhnacademy.heukbaekfrontend.bookCategory.service;

import com.nhnacademy.heukbaekfrontend.category.dto.response.ParentCategoryResponse;

import java.util.List;

public interface BookCategoryService {

    List<ParentCategoryResponse> getBookCategoriesByBookId(Long bookId);
}
