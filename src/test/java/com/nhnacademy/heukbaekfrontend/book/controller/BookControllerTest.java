package com.nhnacademy.heukbaekfrontend.book.controller;

import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import com.nhnacademy.heukbaekfrontend.bookCategory.service.BookCategoryService;
import com.nhnacademy.heukbaekfrontend.category.service.CategoryService;
import com.nhnacademy.heukbaekfrontend.common.filter.ReissueFilter;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private BookCategoryService bookCategoryService;

    @MockBean
    private CookieUtil cookieUtil;

    @MockBean
    private ReissueFilter reissueFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new BookController(bookService, categoryService, bookCategoryService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }
}
