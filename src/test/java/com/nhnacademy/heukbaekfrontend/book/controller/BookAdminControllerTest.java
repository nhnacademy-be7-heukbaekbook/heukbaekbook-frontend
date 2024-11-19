package com.nhnacademy.heukbaekfrontend.book.controller;

import com.nhnacademy.heukbaekfrontend.book.dto.request.BookCreateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.request.BookUpdateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.response.*;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import com.nhnacademy.heukbaekfrontend.category.service.CategoryService;
import com.nhnacademy.heukbaekfrontend.common.filter.JwtAuthenticationFilter;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.tag.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookAdminController.class)
class BookAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CookieUtil cookieUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private TagService tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new BookAdminController(bookService, categoryService, tagService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testShowRegisterBookForm() throws Exception {
        mockMvc.perform(get("/admin/books/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/registerBook"))
                .andExpect(model().attributeExists("bookCreateRequest"));
    }

    @Test
    void testRegisterBook_Success() throws Exception {
        BookCreateRequest request = new BookCreateRequest("title", "index", "description", "publication", "isbn", "imageUrl", false, 10, 100, 0.1f, "publisher", "categories", "authors");
        when(bookService.registerBook(any(BookCreateRequest.class))).thenReturn(ResponseEntity.ok(new BookCreateResponse("title", "index", "description", "publication", "isbn", false, 10, 100, 0.1f, "publisher", List.of("categories"), List.of("authors"))));

        mockMvc.perform(post("/admin/books/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", request.title())
                        .param("index", request.index())
                        .param("description", request.description())
                        .param("publication", request.publication())
                        .param("isbn", request.isbn())
                        .param("standardPrice", String.valueOf(request.standardPrice()))
                        .param("stock", String.valueOf(request.stock()))
                        .param("discountRate", String.valueOf(request.discountRate()))
                        .param("publisher", request.publisher()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/registerBook"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    void testUpdateBookForm() throws Exception {
        Long bookId = 1L;
        BookDetailResponse response = new BookDetailResponse(bookId, "title", "index", "description", "publication", "isbn", "thumbnailImageUrl", List.of(), false, 10, 100, 0.1f, "AVAILABLE", "publisher", List.of("category"), List.of("author"), List.of());
        when(bookService.getBookById(bookId)).thenReturn(response);

        mockMvc.perform(get("/admin/books/{book-id}", bookId))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/updateBook"))
                .andExpect(model().attributeExists("bookUpdateRequest"))
                .andExpect(model().attributeExists("bookId"));
    }

    @Test
    void testUpdateBook_Success() throws Exception {
        Long bookId = 1L;
        BookUpdateRequest request = new BookUpdateRequest("title", "index", "description", "publication", "isbn", "thumbnailUrl", List.of(), false, 10, 100, 0.1f, "AVAILABLE", "publisher", List.of("category"), "author", List.of());
        when(bookService.updateBook(eq(bookId), any(BookUpdateRequest.class))).thenReturn(ResponseEntity.ok(new BookUpdateResponse("title", "index", "description", "publication", "isbn", "imageUrl", false, 10, 100, 0.1f, "AVAILABLE", "publisher", List.of("categories"), List.of("authors"))));

        mockMvc.perform(put("/admin/books/{book-id}", bookId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", request.getTitle())
                        .param("index", request.getIndex())
                        .param("description", request.getDescription())
                        .param("publication", request.getPublication())
                        .param("isbn", request.getIsbn())
                        .param("standardPrice", String.valueOf(request.getStandardPrice()))
                        .param("stock", String.valueOf(request.getStock()))
                        .param("discountRate", String.valueOf(request.getDiscountRate()))
                        .param("publisher", request.getPublisher()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/updateBook"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    void testDeleteBook_Success() throws Exception {
        Long bookId = 1L;
        when(bookService.deleteBook(bookId)).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/admin/books/{book-id}", bookId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/admin/books**"));
    }

    @Test
    void testSearchBooks() throws Exception {
        String title = "Spring Boot";
        List<BookSearchResponse> searchResults = List.of(
                new BookSearchResponse("Spring Boot in Action", "url", "description", "category", "author", "publisher", LocalDate.of(2023, 1, 1), "123456789", 10000, 9000)
        );

        when(bookService.searchBooks(title)).thenReturn(searchResults);

        mockMvc.perform(post("/admin/aladin")
                        .param("title", title))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/searchBookFromAladin"))
                .andExpect(model().attributeExists("responses"))
                .andExpect(model().attribute("responses", searchResults));
    }

    @Test
    void testSelectBookForRegistration() throws Exception {
        BookCreateRequest request = new BookCreateRequest("title", "index", "description", "publication", "isbn", "imageUrl", false, 10, 100, 0.1f, "publisher", "categories", "authors");

        mockMvc.perform(post("/admin/aladin/register")
                        .flashAttr("bookCreateRequest", request))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/registerBook"))
                .andExpect(model().attributeExists("bookCreateRequest"))
                .andExpect(model().attribute("bookCreateRequest", request));
    }

    @Test
    void testViewAllBooks() throws Exception {
        Page<BookDetailResponse> books = new PageImpl<>(List.of(
                new BookDetailResponse(1L, "title", "index", "description", "publication", "isbn", "imageUrl", List.of(), false, 10, 100, 0.1f, "AVAILABLE", "publisher", List.of("category"), List.of("author"), List.of())
        ));

        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(books);

        mockMvc.perform(get("/admin/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/viewAllBooks"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books));
    }
}
