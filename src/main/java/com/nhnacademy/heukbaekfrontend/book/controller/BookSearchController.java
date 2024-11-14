package com.nhnacademy.heukbaekfrontend.book.controller;

import com.nhnacademy.heukbaekfrontend.book.dto.response.BookResponse;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.ACCESS_TOKEN;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookSearchController {

    private final BookService bookService;

    @GetMapping("/search")
    public String searchBooks(@RequestParam("keyword") String keyword,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "25") int size,
                              Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BookResponse> searchResults = bookService.searchBooksWithPagination(keyword, pageable);

        model.addAttribute("page", searchResults);  // `page` 변수로 검색 결과 리스트 추가
        model.addAttribute("keyword", keyword);

        return "home"; // 검색 결과를 표시할 뷰 이름
    }
}
