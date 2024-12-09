//package com.nhnacademy.heukbaekfrontend.book.controller;
//
//import com.nhnacademy.heukbaekfrontend.book.dto.response.BookResponse;
//import com.nhnacademy.heukbaekfrontend.book.dto.response.BookViewResponse;
//import com.nhnacademy.heukbaekfrontend.book.service.BookService;
//import com.nhnacademy.heukbaekfrontend.bookCategory.service.BookCategoryService;
//import com.nhnacademy.heukbaekfrontend.category.dto.response.CategorySummaryResponse;
//import com.nhnacademy.heukbaekfrontend.category.dto.response.ParentCategoryResponse;
//import com.nhnacademy.heukbaekfrontend.category.service.CategoryService;
//import com.nhnacademy.heukbaekfrontend.contributor.dto.ContributorSummaryResponse;
//import com.nhnacademy.heukbaekfrontend.publisher.dto.PublisherSummaryResponse;
//import jakarta.servlet.ServletException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(BookController.class)
//class BookControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private BookService bookService;
//
//    @MockBean
//    private CategoryService categoryService;
//
//    @MockBean
//    private BookCategoryService bookCategoryService;
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(new BookController(bookService, categoryService, bookCategoryService))
//                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
//                .build();
//    }
//
//    @Test
//    void testViewBooksByCategory_Success() throws Exception {
//        Page<BookResponse> mockPage = new PageImpl<>(List.of(
//                new BookResponse(
//                        1L,
//                        "Spring Boot in Action",
//                        "2023-11-01",
//                        "50000",
//                        BigDecimal.valueOf(0.2),
//                        "http://example.com/thumbnail.jpg",
//                        List.of(new ContributorSummaryResponse(1L, "Author1")),
//                        new PublisherSummaryResponse(1L, "Publisher Name")
//                )
//        ));
//
//        List<CategorySummaryResponse> mockCategories = List.of(
//                new CategorySummaryResponse(1L, "Category1", List.of())
//        );
//
//        when(bookService.getBooksByCategoryId(anyLong(), any(Pageable.class))).thenReturn(mockPage);
//        when(categoryService.getTopCategories()).thenReturn(mockCategories);
//
//        mockMvc.perform(get("/books/category")
//                        .param("categoryId", "1")
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("home"))
//                .andExpect(model().attributeExists("page"))
//                .andExpect(model().attributeExists("categories"));
//    }
//
//    @Test
//    void testViewBooksByCategory_NotFound() {
//        when(bookService.getBooksByCategoryId(anyLong(), any(Pageable.class))).thenThrow(new RuntimeException("Category not found"));
//
//        ServletException exception = assertThrows(ServletException.class, () ->
//                mockMvc.perform(get("/books/category")
//                                .param("categoryId", "999"))
//                        .andExpect(status().isInternalServerError())
//        );
//
//        assertEquals("Category not found", exception.getCause().getMessage());
//    }
//
//    @Test
//    void testViewBookDetail_Success() throws Exception {
//        BookViewResponse mockBook = new BookViewResponse(
//                1L,
//                "Spring Boot in Action",
//                "001",
//                "Detailed description of Spring Boot",
//                "2023-11-01",
//                "123456789",
//                true,
//                "60000",
//                "50000",
//                BigDecimal.valueOf(0.2),
//                100,
//                "AVAILABLE",
//                "http://example.com/thumbnail.jpg",
//                List.of("http://example.com/detail1.jpg", "http://example.com/detail2.jpg"),
//                List.of(new ContributorSummaryResponse(1L, "Author1")),
//                new PublisherSummaryResponse(1L, "Publisher Name")
//        );
//
//        List<ParentCategoryResponse> mockCategories = List.of(
//                new ParentCategoryResponse(1L, "Parent Category")
//        );
//
//        when(bookService.getBookDetailByBookId(anyLong())).thenReturn(mockBook);
//        when(bookCategoryService.getBookCategoriesByBookId(anyLong())).thenReturn(mockCategories);
//
//        mockMvc.perform(get("/books/detail")
//                        .param("bookId", "1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("book/detail"))
//                .andExpect(model().attributeExists("book"))
//                .andExpect(model().attribute("book", mockBook))
//                .andExpect(model().attributeExists("categories"))
//                .andExpect(model().attribute("categories", mockCategories))
//                .andExpect(model().attributeExists("availableCoupons"));
//    }
//
//    @Test
//    void testViewBookDetail_NotFound() {
//        when(bookService.getBookDetailByBookId(anyLong())).thenThrow(new RuntimeException("Book not found"));
//
//        ServletException exception = assertThrows(ServletException.class, () ->
//                mockMvc.perform(get("/books/detail")
//                                .param("bookId", "999"))
//                        .andExpect(status().isInternalServerError())
//        );
//
//        assertEquals("Book not found", exception.getCause().getMessage());
//    }
//}
