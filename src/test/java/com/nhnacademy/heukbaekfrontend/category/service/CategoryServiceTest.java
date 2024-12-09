package com.nhnacademy.heukbaekfrontend.category.service;

import com.nhnacademy.heukbaekfrontend.category.client.CategoryAdmin;
import com.nhnacademy.heukbaekfrontend.category.client.CategoryClient;
import com.nhnacademy.heukbaekfrontend.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.heukbaekfrontend.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.heukbaekfrontend.category.dto.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryAdmin categoryAdmin;

    @Mock
    private CategoryClient categoryClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() {
        Pageable pageable = mock(Pageable.class);
        Page<CategoryDetailResponse> categoryPage = new PageImpl<>(List.of(
                new CategoryDetailResponse(1L, 0L, "Category A")
        ));

        when(categoryAdmin.getCategories(any(Pageable.class))).thenReturn(categoryPage);

        Page<CategoryDetailResponse> result = categoryService.getAllCategories(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Category A");
        verify(categoryAdmin, times(1)).getCategories(pageable);
    }

    @Test
    void testGetCategoryById() {
        CategoryDetailResponse category = new CategoryDetailResponse(1L, 0L, "Category A");

        when(categoryAdmin.getCategory(anyLong())).thenReturn(category);

        CategoryDetailResponse result = categoryService.getCategoryById(1L);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Category A");
        verify(categoryAdmin, times(1)).getCategory(1L);
    }

    @Test
    void testRegisterCategory() {
        CategoryCreateRequest request = new CategoryCreateRequest(0L, "New Category");
        CategoryCreateResponse response = new CategoryCreateResponse(1L, "New Category");

        when(categoryAdmin.createCategory(any(CategoryCreateRequest.class)))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<CategoryCreateResponse> result = categoryService.registerCategory(request);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("New Category");
        verify(categoryAdmin, times(1)).createCategory(request);
    }

    @Test
    void testUpdateCategory() {
        CategoryUpdateRequest request = new CategoryUpdateRequest(0L, "Updated Category");
        CategoryUpdateResponse response = new CategoryUpdateResponse(1L, "Updated Category");

        when(categoryAdmin.updateCategory(anyLong(), any(CategoryUpdateRequest.class)))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<CategoryUpdateResponse> result = categoryService.updateCategory(1L, request);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("Updated Category");
        verify(categoryAdmin, times(1)).updateCategory(1L, request);
    }

    @Test
    void testDeleteCategory() {
        CategoryDeleteResponse response = new CategoryDeleteResponse("Category deleted successfully");

        when(categoryAdmin.deleteCategory(anyLong()))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<CategoryDeleteResponse> result = categoryService.deleteBook(1L);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().message()).isEqualTo("Category deleted successfully");
        verify(categoryAdmin, times(1)).deleteCategory(1L);
    }

    @Test
    void testGetTopCategories() {
        List<CategorySummaryResponse> mockTopCategories = List.of(
                new CategorySummaryResponse(1L, "Category A", List.of(
                        new CategorySummaryResponse(2L, "Subcategory A1", List.of())
                )),
                new CategorySummaryResponse(3L, "Category B", List.of())
        );

        when(categoryClient.getCategories()).thenReturn(mockTopCategories);

        List<CategorySummaryResponse> result = categoryService.getTopCategories();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Category A");
        assertThat(result.get(0).subCategorySummaryResponses()).hasSize(1);
        assertThat(result.get(1).name()).isEqualTo("Category B");
        verify(categoryClient, times(1)).getCategories();
    }

    @Test
    void testGetCategoryPaths() {
        List<String> mockPaths = List.of("Category A > Subcategory A1", "Category B > Subcategory B1");

        when(categoryAdmin.getCategoryPaths()).thenReturn(ResponseEntity.ok(mockPaths));

        List<String> result = categoryService.getCategoryPaths();

        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo("Category A > Subcategory A1");
        assertThat(result.get(1)).isEqualTo("Category B > Subcategory B1");
        verify(categoryAdmin, times(1)).getCategoryPaths();
    }

}
