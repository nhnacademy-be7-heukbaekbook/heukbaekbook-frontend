package com.nhnacademy.heukbaekfrontend;

import com.nhnacademy.heukbaekfrontend.book.dto.request.BookSearchRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookResponse;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategorySummaryResponse;
import com.nhnacademy.heukbaekfrontend.category.service.CategoryService;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.ACCESS_TOKEN;


@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final CookieUtil cookieUtil;

    private final BookService bookService;

    private final CategoryService categoryService;


    @GetMapping("/")
    public ModelAndView home(HttpServletRequest request,
                             Pageable pageable) {
        String accessToken = cookieUtil.getCookieValue(request, ACCESS_TOKEN);
        boolean isLogin = accessToken != null;
        log.info("isLogin: {}, pageable : {}", isLogin, pageable);

        ModelAndView modelAndView = new ModelAndView("home");

        Page<BookResponse> page = bookService.getBooks(pageable);
        List<CategorySummaryResponse> categories = categoryService.getTopCategories();

        modelAndView
                .addObject("page", page)
                .addObject("isLogin", isLogin)
                .addObject("categories", categories);

        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView searchBooks(HttpServletRequest request, BookSearchRequest bookSearchRequest, @PageableDefault(page = 0, size = 10) Pageable pageable ) {
        String accessToken = cookieUtil.getCookieValue(request, ACCESS_TOKEN);
        boolean isLogin = accessToken != null;
        log.info("isLogin: {}", isLogin);

        Page<BookResponse> page = bookService.searchElasticBooks(bookSearchRequest, pageable);
        List<CategorySummaryResponse> categories = categoryService.getTopCategories();


        ModelAndView modelAndView = new ModelAndView("home"); // 기존 home.html 사용
        modelAndView
                .addObject("page", page)
                .addObject("isLogin", isLogin)
                .addObject("categories", categories);;
        return modelAndView;
    }

}
