package com.nhnacademy.heukbaekfrontend.category.controller;

import com.nhnacademy.heukbaekfrontend.category.controller.admin.CategoryAdminController;
import com.nhnacademy.heukbaekfrontend.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.heukbaekfrontend.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryCreateResponse;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryDeleteResponse;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryDetailResponse;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryUpdateResponse;
import com.nhnacademy.heukbaekfrontend.category.service.CategoryService;
import com.nhnacademy.heukbaekfrontend.common.filter.ReissueFilter;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.service.CouponPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryAdminController.class)
class CategoryAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CouponPolicyService couponPolicyService;

    @MockBean
    private CookieUtil cookieUtil;

    @MockBean
    private ReissueFilter reissueFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new CategoryAdminController(categoryService, couponPolicyService)).build();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new CategoryAdminController(categoryService, couponPolicyService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testViewAllCategories() throws Exception {
        Page<CategoryDetailResponse> categories = new PageImpl<>(List.of(new CategoryDetailResponse(1L, 0L, "CategoryName")));
        when(categoryService.getAllCategories(any(PageRequest.class))).thenReturn(categories);

        mockMvc.perform(get("/admin/categories").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("category/admin/viewAllCategories"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    void testViewRegisterCategoryForm() throws Exception {
        Page<CategoryDetailResponse> categories = new PageImpl<>(List.of(new CategoryDetailResponse(1L, 0L, "CategoryName")));
        when(categoryService.getAllCategories(any(PageRequest.class))).thenReturn(categories);

        mockMvc.perform(get("/admin/categories/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("category/admin/registerCategory"))
                .andExpect(model().attributeExists("categoryCreateRequest"))
                .andExpect(model().attributeExists("allCategories"));
    }

    @Test
    void testRegisterCategory_Success() throws Exception {
        when(categoryService.registerCategory(any(CategoryCreateRequest.class))).thenReturn(ResponseEntity.ok(new CategoryCreateResponse(0L, "New Category")));

        mockMvc.perform(post("/admin/categories/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("parentId", "0")
                        .param("name", "New Category"))
                .andExpect(status().isOk())
                .andExpect(view().name("category/admin/registerCategory"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    void testUpdateCategoryForm() throws Exception {
        Long categoryId = 1L;
        CategoryDetailResponse response = new CategoryDetailResponse(categoryId, 0L, "CategoryName");
        Page<CategoryDetailResponse> allCategories = new PageImpl<>(List.of(response));
        when(categoryService.getCategoryById(categoryId)).thenReturn(response);
        when(categoryService.getAllCategories(any(PageRequest.class))).thenReturn(allCategories);

        mockMvc.perform(get("/admin/categories/{category-id}/update", categoryId))
                .andExpect(status().isOk())
                .andExpect(view().name("category/admin/updateCategory"))
                .andExpect(model().attributeExists("categoryUpdateRequest"))
                .andExpect(model().attributeExists("allCategories"));
    }

    @Test
    void testUpdateCategory_Success() throws Exception {
        Long categoryId = 1L;
        when(categoryService.updateCategory(any(Long.class), any(CategoryUpdateRequest.class))).thenReturn(ResponseEntity.ok(new CategoryUpdateResponse(0L, "Updated Category")));

        mockMvc.perform(put("/admin/categories/{category-id}", categoryId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("parentId", "0")
                        .param("name", "Updated Category"))
                .andExpect(status().isOk())
                .andExpect(view().name("category/admin/updateCategory"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    void testDeleteCategory_Success() throws Exception {
        Long categoryId = 1L;
        when(categoryService.deleteBook(categoryId)).thenReturn(ResponseEntity.ok(new CategoryDeleteResponse("Category deleted successfully")));

        mockMvc.perform(delete("/admin/categories/{category-id}", categoryId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/admin/categories**"));
    }
}
