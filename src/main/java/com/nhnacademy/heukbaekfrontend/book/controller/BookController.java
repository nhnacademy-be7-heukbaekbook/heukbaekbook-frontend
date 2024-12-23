package com.nhnacademy.heukbaekfrontend.book.controller;

import com.nhnacademy.heukbaekfrontend.book.dto.response.BookResponse;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookViewResponse;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import com.nhnacademy.heukbaekfrontend.bookCategory.service.BookCategoryService;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategorySummaryResponse;
import com.nhnacademy.heukbaekfrontend.category.dto.response.ParentCategoryResponse;
import com.nhnacademy.heukbaekfrontend.category.service.CategoryService;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.service.CouponService;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekfrontend.review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final BookCategoryService bookCategoryService;
    private final ReviewService reviewService;
    private final CouponService couponService;

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

        BookViewResponse bookViewResponse = bookService.getBookDetailByBookId(bookId);
        List<ParentCategoryResponse> parentCategoryResponses = bookCategoryService.getBookCategoriesByBookId(bookId);
        List<ReviewDetailResponse> reviews = reviewService.getMyReviewsByBook(bookId);
        List<CouponResponse> downloadableCoupons = couponService.getDownloadableCouponsByBookId(bookId);

        log.info("parentCategoryResponses : {}", parentCategoryResponses);
        log.info("reviews : {}", reviews);

        modelAndView
                .addObject("book", bookViewResponse)
                .addObject("categories", parentCategoryResponses)
                .addObject("reviews", reviews)
                .addObject("availableCoupons", downloadableCoupons);

        return modelAndView;
    }
}
