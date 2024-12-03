package com.nhnacademy.heukbaekfrontend;

import com.nhnacademy.heukbaekfrontend.book.dto.request.BookSearchRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookResponse;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategorySummaryResponse;
import com.nhnacademy.heukbaekfrontend.category.service.CategoryService;
import com.nhnacademy.heukbaekfrontend.common.filter.ReissueFilter;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.contributor.dto.ContributorSummaryResponse;
import com.nhnacademy.heukbaekfrontend.publisher.dto.PublisherSummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CookieUtil cookieUtil;

    @MockBean
    private BookService bookService;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser // Mock 인증 사용자 추가
    void testHome() throws Exception {
        // Mock 데이터 설정
        mockServiceForHome();

        // 요청 및 검증
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("isLogin", true))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    @WithMockUser // Mock 인증 사용자 추가
    void testSearchBooks() throws Exception {
        // Mock 데이터 설정
        mockServiceForSearch();

        // 요청 및 검증
        mockMvc.perform(get("/search")
                        .param("query", "Book B")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("isLogin", false))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attributeExists("categories"));
    }

    private void mockServiceForHome() {
        Page<BookResponse> mockPage = new PageImpl<>(
                List.of(new BookResponse(
                        1L,
                        "Book A",
                        "2024-01-01",
                        "10000",
                        BigDecimal.valueOf(0.1),
                        "http://example.com/thumbnail.jpg",
                        List.of(new ContributorSummaryResponse(1L, "Author A")),
                        new PublisherSummaryResponse(1L, "Publisher A")
                )),
                PageRequest.of(0, 10),
                1
        );
        List<CategorySummaryResponse> mockCategories = List.of(
                new CategorySummaryResponse(1L, "Category A", List.of())
        );

        when(cookieUtil.getCookieValue(any(), anyString())).thenReturn("mockAccessToken");
        when(bookService.getBooks(any())).thenReturn(mockPage);
        when(categoryService.getTopCategories()).thenReturn(mockCategories);
    }

    private void mockServiceForSearch() {
        Page<BookResponse> mockPage = new PageImpl<>(
                List.of(new BookResponse(
                        2L,
                        "Book B",
                        "2024-02-01",
                        "15000",
                        BigDecimal.valueOf(0.15),
                        "http://example.com/thumbnail_b.jpg",
                        List.of(new ContributorSummaryResponse(2L, "Author B")),
                        new PublisherSummaryResponse(2L, "Publisher B")
                )),
                PageRequest.of(0, 10),
                1
        );
        List<CategorySummaryResponse> mockCategories = List.of(
                new CategorySummaryResponse(2L, "Category B", List.of())
        );

        when(cookieUtil.getCookieValue(any(), anyString())).thenReturn(null); // 로그인하지 않은 상태
        when(bookService.searchElasticBooks(any(BookSearchRequest.class), any())).thenReturn(mockPage);
        when(categoryService.getTopCategories()).thenReturn(mockCategories);
    }
}
