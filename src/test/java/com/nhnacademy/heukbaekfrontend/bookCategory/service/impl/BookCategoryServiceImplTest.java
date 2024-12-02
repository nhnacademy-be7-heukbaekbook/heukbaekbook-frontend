package com.nhnacademy.heukbaekfrontend.bookCategory.service.impl;

import com.nhnacademy.heukbaekfrontend.bookCategory.service.client.BookCategoryClient;
import com.nhnacademy.heukbaekfrontend.category.dto.response.ParentCategoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookCategoryServiceImplTest {

    private BookCategoryServiceImpl bookCategoryService;

    @Mock
    private BookCategoryClient bookCategoryClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookCategoryService = new BookCategoryServiceImpl(bookCategoryClient);
    }

    @Test
    void testGetBookCategoriesByBookId() {
        // Arrange
        Long bookId = 1L;
        List<ParentCategoryResponse> mockResponses = List.of(
                new ParentCategoryResponse(1L, "Fiction"),
                new ParentCategoryResponse(2L, "Science Fiction")
        );
        when(bookCategoryClient.getBookCategoriesByBookId(bookId)).thenReturn(mockResponses);

        // Act
        List<ParentCategoryResponse> categories = bookCategoryService.getBookCategoriesByBookId(bookId);

        // Assert
        assertThat(categories).hasSize(2);
        assertThat(categories.get(0).name()).isEqualTo("Fiction");
        assertThat(categories.get(1).name()).isEqualTo("Science Fiction");
        verify(bookCategoryClient, times(1)).getBookCategoriesByBookId(bookId);
    }
}
