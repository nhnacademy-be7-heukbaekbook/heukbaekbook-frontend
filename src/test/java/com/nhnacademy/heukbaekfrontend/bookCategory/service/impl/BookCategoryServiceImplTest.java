package com.nhnacademy.heukbaekfrontend.bookCategory.service.impl;

import com.nhnacademy.heukbaekfrontend.bookCategory.service.client.BookCategoryClient;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategorySummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BookCategoryServiceImplTest {

    @InjectMocks
    private BookCategoryServiceImpl bookCategoryService;

    @Mock
    private BookCategoryClient bookCategoryClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testGetBookCategoriesByBookId() {
//
//        List<CategorySummaryResponse> mockCategories = List.of(
//                new CategorySummaryResponse(1L, "Category1", List.of()),
//                new CategorySummaryResponse(2L, "Category2", List.of())
//        );
//
//        when(bookCategoryClient.getBookCategoriesByBookId(anyLong())).thenReturn(mockCategories);
//
//
//        List<CategorySummaryResponse> result = bookCategoryService.getBookCategoriesByBookId(1L);
//
//        assertThat(result).isNotNull();
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).id()).isEqualTo(1L);
//        assertThat(result.get(0).name()).isEqualTo("Category1");
//        assertThat(result.get(1).id()).isEqualTo(2L);
//        assertThat(result.get(1).name()).isEqualTo("Category2");
//
//        verify(bookCategoryClient, times(1)).getBookCategoriesByBookId(1L);
//    }
}
