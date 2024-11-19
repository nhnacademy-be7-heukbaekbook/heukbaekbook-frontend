package com.nhnacademy.heukbaekfrontend.bookCategory.service.impl;

import com.nhnacademy.heukbaekfrontend.bookCategory.service.BookCategoryService;
import com.nhnacademy.heukbaekfrontend.bookCategory.service.client.BookCategoryClient;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategorySummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCategoryServiceImpl implements BookCategoryService {

    private final BookCategoryClient bookCategoryClient;

    @Override
    public List<CategorySummaryResponse> getBookCategoriesByBookId(Long bookId) {
        return bookCategoryClient.getBookCategoriesByBookId(bookId);
    }
}
