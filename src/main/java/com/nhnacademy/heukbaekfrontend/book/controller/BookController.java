package com.nhnacademy.heukbaekfrontend.book.controller;

import com.nhnacademy.heukbaekfrontend.book.dto.response.BookDetailResponse;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookResponse;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategorySummaryResponse;
import com.nhnacademy.heukbaekfrontend.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
@Slf4j
public class BookController {

    private final BookService bookService;

    private final CategoryService categoryService;

    @GetMapping("/{book-id}")
    public String viewBook(@PathVariable(name = "book-id") Long bookId, Model model) {
        BookDetailResponse bookDetail = bookService.getBookById(bookId);
        model.addAttribute("book", bookDetail);
        return "admin/bookDetail";
    }

    @GetMapping("/category")
    public ModelAndView viewBooksByCategory(@RequestParam Long categoryId,
                                            Pageable pageable) {
        log.info("categoryId : {}, pageable : {}", categoryId, pageable);
        ModelAndView modelAndView = new ModelAndView("home");

        Page<BookResponse> page = bookService.getBooksByCategoryId(categoryId, pageable);
        modelAndView.addObject("page", page);

        List<CategorySummaryResponse> categories = categoryService.getTopCategories();
        modelAndView.addObject("categories", categories);

        return modelAndView;
    }

    @GetMapping("/detail")
    public ModelAndView viewBookDetail(@RequestParam Long bookId) {
        log.info("bookId : {}", bookId);
        ModelAndView modelAndView = new ModelAndView("book/detail");

        BookResponse bookResponse = bookService.getBookDetailByBookId(bookId);
        return modelAndView;
    }

}