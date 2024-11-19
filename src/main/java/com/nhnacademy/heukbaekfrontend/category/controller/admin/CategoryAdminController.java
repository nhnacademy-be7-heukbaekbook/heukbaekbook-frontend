package com.nhnacademy.heukbaekfrontend.category.controller.admin;

import com.nhnacademy.heukbaekfrontend.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.heukbaekfrontend.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryCreateResponse;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryDeleteResponse;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryDetailResponse;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategoryUpdateResponse;
import com.nhnacademy.heukbaekfrontend.category.service.CategoryService;
import com.nhnacademy.heukbaekfrontend.common.annotation.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.nhnacademy.heukbaekfrontend.util.Utils.getRedirectUrl;

@Controller
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Admin
    @GetMapping
    public String viewAllCategories(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            Model model
    ) {
        Page<CategoryDetailResponse> categories = categoryService.getAllCategories(pageable);
        model.addAttribute("categories", categories);
        return "admin/viewAllCategories";
    }

    @Admin
    @GetMapping("/register")
    public String viewRegisterCategoryForm(Model model, Pageable pageable) {
        Page<CategoryDetailResponse> categories = categoryService.getAllCategories(pageable);
        model.addAttribute("categoryCreateRequest", new CategoryCreateRequest(0L, ""));
        model.addAttribute("allCategories", categories);
        return "admin/registerCategory";
    }

    @Admin
    @PostMapping("/register")
    public String registerCategory(@ModelAttribute CategoryCreateRequest request, Model model) {
        ResponseEntity<CategoryCreateResponse> response = categoryService.registerCategory(request);

        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", "도서 등록에 실패했습니다.");
            model.addAttribute("bookCreateRequest", request);
        }
        return "admin/registerCategory";
    }

    @Admin
    @GetMapping("/{category-id}/update")
    public String updateCategoryForm(@PathVariable(name = "category-id") Long categoryId,
                                     Model model,
                                     Pageable pageable) {
        CategoryDetailResponse categoryDetail = categoryService.getCategoryById(categoryId);
        CategoryUpdateRequest request = new CategoryUpdateRequest(
                categoryDetail.parentId(),
                categoryDetail.name()
        );
        Page<CategoryDetailResponse> allCategories = categoryService.getAllCategories(pageable);

        model.addAttribute("categoryUpdateRequest", request);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("allCategories", allCategories);
        return "admin/updateCategory";
    }


    @Admin
    @PutMapping("/{category-id}")
    public String updateCategory(@PathVariable(name = "category-id") Long categoryId,
                                 @ModelAttribute CategoryUpdateRequest request,
                                 Model model) {
        ResponseEntity<CategoryUpdateResponse> response = categoryService.updateCategory(categoryId, request);

        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", "카테고리 수정에 실패했습니다.");
            model.addAttribute("CategoryUpdateRequest", request);
        }
        return "admin/updateCategory";
    }

    @Admin
    @DeleteMapping("/{category-id}")
    public String deleteBook(
            @PathVariable(name = "category-id") Long categoryId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort", required = false) String sort,
            RedirectAttributes redirectAttributes) {

        ResponseEntity<CategoryDeleteResponse> response = categoryService.deleteBook(categoryId);

        if (response.getStatusCode().is2xxSuccessful()) {
            redirectAttributes.addFlashAttribute("success", true);
        } else {
            redirectAttributes.addFlashAttribute("error", "카테고리 삭제에 실패했습니다.");
        }

        String redirectUrl = getRedirectUrl(page, size, sort, "/admin/categories");

        return "redirect:" + redirectUrl;
    }
}
