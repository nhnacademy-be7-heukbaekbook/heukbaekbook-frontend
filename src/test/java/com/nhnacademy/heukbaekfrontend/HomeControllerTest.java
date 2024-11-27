//package com.nhnacademy.heukbaekfrontend;
//
//import com.nhnacademy.heukbaekfrontend.book.controller.BookAdminController;
//import com.nhnacademy.heukbaekfrontend.book.dto.response.BookResponse;
//import com.nhnacademy.heukbaekfrontend.book.service.BookService;
//import com.nhnacademy.heukbaekfrontend.category.dto.response.CategorySummaryResponse;
//import com.nhnacademy.heukbaekfrontend.category.service.CategoryService;
//import com.nhnacademy.heukbaekfrontend.common.client.AuthClient;
//import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(HomeController.class)
//class HomeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CookieUtil cookieUtil;
//
//    @MockBean
//    private BookService bookService;
//
//    @MockBean
//    private CategoryService categoryService;
//
//    @MockBean
//    private AuthClient authClient;
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(new HomeController(cookieUtil,bookService, categoryService))
//                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
//                .build();
//    }
//
//
//    @Test
//    void testHome() throws Exception {
//        when(cookieUtil.getCookie(any(), eq("accessToken"))).thenReturn("mockAccessToken");
//
//        Page<BookResponse> mockPage = new PageImpl<>(List.of(
//                new BookResponse(1L, "Book 1", "2024-01-01", "10000", new BigDecimal(10), "url1", List.of(), null)
//        ));
//        when(bookService.getBooks(any())).thenReturn(mockPage);
//
//        mockMvc.perform(get("/"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("home"))
//                .andExpect(model().attributeExists("page"));
//    }
//
//    @Test
//    void testSearchBooks() throws Exception {
//
//        when(cookieUtil.getCookie(any(), eq("accessToken"))).thenReturn("mockAccessToken");
//
//        Page<BookResponse> mockPage = new PageImpl<>(
//                List.of(
//                        new BookResponse(1L, "Searched Book 1", "2024-01-01", "10000", new BigDecimal(10), "url1", List.of(), null)
//                ),
//                PageRequest.of(0, 10),
//                1
//        );
//        when(bookService.searchElasticBooks(any(), any())).thenReturn(mockPage);
//
//
//        mockMvc.perform(get("/search")
//                        .param("title", "searchTitle")
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("home"))
//                .andExpect(model().attributeExists("page", "isLogin"))
//                .andExpect(model().attribute("isLogin", true))
//                .andExpect(model().attribute("page", mockPage));
//    }
//}
