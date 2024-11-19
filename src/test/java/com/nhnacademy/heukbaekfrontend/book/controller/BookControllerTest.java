package com.nhnacademy.heukbaekfrontend.book.controller;

import com.nhnacademy.heukbaekfrontend.book.dto.response.BookDetailResponse;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import com.nhnacademy.heukbaekfrontend.category.service.CategoryService;
import com.nhnacademy.heukbaekfrontend.common.filter.JwtAuthenticationFilter;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CookieUtil cookieUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new BookController(bookService, categoryService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testViewBook() throws Exception {
        Long bookId = 1L;
        BookDetailResponse response = new BookDetailResponse(
                bookId,
                "Spring Boot in Action",
                "001",
                "Detailed description of Spring Boot",
                "2023-11-01",
                "123456789",
                "http://example.com/thumbnail.jpg",
                List.of("http://example.com/detail1.jpg", "http://example.com/detail2.jpg"),
                true,
                10,
                20000,
                0.1f,
                "AVAILABLE",
                "Publisher Name",
                List.of("Category1", "Category2"),
                List.of("Author1", "Author2"),
                List.of("Tag1", "Tag2")
        );

        when(bookService.getBookById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/books/{book-id}", bookId))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/bookDetail"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", response));
    }
}
