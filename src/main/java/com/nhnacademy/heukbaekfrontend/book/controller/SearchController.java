package com.nhnacademy.heukbaekfrontend.book.controller;

import com.nhnacademy.heukbaekfrontend.book.dto.response.BookResponse;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final BookService bookService;

    @GetMapping("/search")
    public String searchBooks(@RequestParam("keyword") String keyword,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "25") int size,
                              Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BookResponse> searchResults = bookService.searchBooksWithPagination(keyword, PageRequest.of(0, 25));

        model.addAttribute("responses", searchResults.getContent());  // 검색 결과 리스트를 추가
        model.addAttribute("currentPage", searchResults.getNumber());
        model.addAttribute("totalPages", searchResults.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "home"; // 검색 결과를 표시할 뷰 이름
    }
}
