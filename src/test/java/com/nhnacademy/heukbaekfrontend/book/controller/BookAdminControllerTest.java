//package com.nhnacademy.heukbaekfrontend.book.controller;
//
//import com.nhnacademy.heukbaekfrontend.book.dto.request.BookCreateRequest;
//import com.nhnacademy.heukbaekfrontend.book.dto.request.BookUpdateRequest;
//import com.nhnacademy.heukbaekfrontend.book.dto.response.BookCreateResponse;
//import com.nhnacademy.heukbaekfrontend.book.dto.response.BookDeleteResponse;
//import com.nhnacademy.heukbaekfrontend.book.dto.response.BookDetailResponse;
//import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSearchResponse;
//import com.nhnacademy.heukbaekfrontend.book.dto.response.BookUpdateResponse;
//import com.nhnacademy.heukbaekfrontend.book.service.BookService;
//import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.*;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static com.nhnacademy.heukbaekfrontend.util.Utils.getRedirectUrl;
//import static org.hamcrest.Matchers.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//@WebMvcTest(BookAdminController.class)
//class BookAdminControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private BookService bookService;
//
//    @MockBean
//    private CookieUtil cookieUtil;
//
//    @Test
//    @DisplayName("GET /admins/aladin - Show search form")
//    void testSearchBooksForm() throws Exception {
//        mockMvc.perform(get("/admins/aladin"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("admin/searchBookFromAladin"));
//    }
//
//    @Test
//    @DisplayName("POST /admins/aladin - Search books by title")
//    void testSearchBooks() throws Exception {
//        String title = "Spring";
//        List<BookSearchResponse> searchResults = Arrays.asList(
//                new BookSearchResponse("Spring in Action", "cover1.jpg", "A comprehensive guide to Spring.",
//                        "Programming", "Craig Walls", "Publisher1", null, "ISBN123456", 5000, 4500)
//        );
//
//        when(bookService.searchBooks(title)).thenReturn(searchResults);
//
//        mockMvc.perform(post("/admins/aladin")
//                        .param("title", title))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("admin/searchBookFromAladin"))
//                .andExpect(MockMvcResultMatchers.model().attribute("responses", hasSize(1)))
//                .andExpect(MockMvcResultMatchers.model().attribute("responses",
//                        hasItem(
//                                allOf(
//                                        hasProperty("title", is("Spring in Action")),
//                                        hasProperty("cover", is("cover1.jpg"))
//                                )
//                        )
//                ));
//
//        verify(bookService, times(1)).searchBooks(title);
//    }
//
//    @Test
//    @DisplayName("POST /admins/aladin/register - Select book for registration")
//    void testSelectBookForRegistration() throws Exception {
//        BookCreateRequest request = new BookCreateRequest("Title1", "Index1", "Desc1", "Pub1",
//                "ISBN1", "image1.jpg", true, 10, 1000, 10.0f, "Publisher1", "Category1", "Author1");
//
//        mockMvc.perform(post("/admins/aladin/register")
//                        .flashAttr("bookCreateRequest", request))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("admin/registerBook"))
//                .andExpect(MockMvcResultMatchers.model().attribute("bookCreateRequest", request));
//    }
//
//    @Test
//    @DisplayName("GET /admins/books - View all books")
//    void testViewAllBooks() throws Exception {
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());
//        List<BookDetailResponse> bookList = Arrays.asList(
//                new BookDetailResponse(1L, "Title1", "Index1", "Desc1", "Pub1", "ISBN1",
//                        "thumb1.jpg", Arrays.asList("detail1.jpg"), true, 10, 1000, 10.0f,
//                        "Available", "Publisher1", Arrays.asList("Category1"), Arrays.asList("Author1"), Arrays.asList("Tag1"))
//        );
//        Page<BookDetailResponse> bookPage = new PageImpl<>(bookList, pageable, bookList.size());
//
//        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(bookPage);
//
//        mockMvc.perform(get("/admins/books")
//                        .param("page", "0")
//                        .param("size", "10")
//                        .param("sort", "title,asc"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("admin/viewAllBooks"))
//                .andExpect(MockMvcResultMatchers.model().attribute("books", hasSize(1)))
//                .andExpect(MockMvcResultMatchers.model().attribute("page", 0))
//                .andExpect(MockMvcResultMatchers.model().attribute("size", 10))
//                .andExpect(MockMvcResultMatchers.model().attribute("sort", "title: ASC"));
//
//        verify(bookService, times(1)).getAllBooks(any(Pageable.class));
//    }
//
//    @Test
//    @DisplayName("GET /admins/books/register - Show register book form")
//    void testShowRegisterBookForm() throws Exception {
//        mockMvc.perform(get("/admins/books/register"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("admin/registerBook"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("bookCreateRequest"));
//    }
//
//    @Test
//    @DisplayName("POST /admins/books/register - Register a new book - Success")
//    void testRegisterBook_Success() throws Exception {
//        BookCreateRequest request = new BookCreateRequest("Title1", "Index1", "Desc1", "Pub1",
//                "ISBN1", "image1.jpg", true, 10, 1000, 10.0f, "Publisher1", "Category1", "Author1");
//
//        BookCreateResponse createResponse = new BookCreateResponse("Title1", "Index1", "Desc1",
//                "Pub1", "ISBN1", true, 10, 1000, 10.0f, "Publisher1",
//                Arrays.asList("Category1"), Arrays.asList("Author1"));
//
//        when(bookService.registerBook(any(BookCreateRequest.class))).thenReturn(ResponseEntity.ok(createResponse));
//
//        mockMvc.perform(post("/admins/books/register")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("title", request.title())
//                        .param("index", request.index())
//                        .param("description", request.description())
//                        .param("publication", request.publication())
//                        .param("isbn", request.isbn())
//                        .param("imageUrl", request.imageUrl())
//                        .param("isPackable", String.valueOf(request.isPackable()))
//                        .param("stock", String.valueOf(request.stock()))
//                        .param("standardPrice", String.valueOf(request.standardPrice()))
//                        .param("discountRate", String.valueOf(request.discountRate()))
//                        .param("publisher", request.publisher())
//                        .param("categories", request.categories())
//                        .param("authors", request.authors()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("admin/registerBook"))
//                .andExpect(MockMvcResultMatchers.model().attribute("success", true));
//
//        verify(bookService, times(1)).registerBook(any(BookCreateRequest.class));
//    }
//
//    @Test
//    @DisplayName("POST /admins/books/register - Register a new book - Failure")
//    void testRegisterBook_Failure() throws Exception {
//        BookCreateRequest request = new BookCreateRequest("Title1", "Index1", "Desc1", "Pub1",
//                "ISBN1", "image1.jpg", true, 10, 1000, 10.0f, "Publisher1", "Category1", "Author1");
//
//        when(bookService.registerBook(any(BookCreateRequest.class))).thenReturn(ResponseEntity.status(500).build());
//
//        mockMvc.perform(post("/admins/books/register")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("title", request.title())
//                        .param("index", request.index())
//                        .param("description", request.description())
//                        .param("publication", request.publication())
//                        .param("isbn", request.isbn())
//                        .param("imageUrl", request.imageUrl())
//                        .param("isPackable", String.valueOf(request.isPackable()))
//                        .param("stock", String.valueOf(request.stock()))
//                        .param("standardPrice", String.valueOf(request.standardPrice()))
//                        .param("discountRate", String.valueOf(request.discountRate()))
//                        .param("publisher", request.publisher())
//                        .param("categories", request.categories())
//                        .param("authors", request.authors()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("admin/registerBook"))
//                .andExpect(MockMvcResultMatchers.model().attribute("error", "도서 등록에 실패했습니다."))
//                .andExpect(MockMvcResultMatchers.model().attribute("bookCreateRequest", request));
//
//        verify(bookService, times(1)).registerBook(any(BookCreateRequest.class));
//    }
//
//    @Test
//    @DisplayName("GET /admins/books/{book-id} - Show update book form")
//    void testUpdateBookForm() throws Exception {
//        Long bookId = 1L;
//        BookDetailResponse bookDetail = new BookDetailResponse(bookId, "Title1", "Index1", "Desc1", "Pub1",
//                "ISBN1", "thumb1.jpg", Arrays.asList("detail1.jpg"), true, 10, 1000, 10.0f,
//                "Available", "Publisher1", Arrays.asList("Category1"), Arrays.asList("Author1"), Arrays.asList("Tag1"));
//
//        when(bookService.getBookById(bookId)).thenReturn(bookDetail);
//
//        mockMvc.perform(get("/admins/books/{book-id}", bookId))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("admin/updateBook"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("bookUpdateRequest"))
//                .andExpect(MockMvcResultMatchers.model().attribute("bookId", bookId));
//
//        verify(bookService, times(1)).getBookById(bookId);
//    }
//
//    @Test
//    @DisplayName("PUT /admins/books/{book-id} - Update book - Success")
//    void testUpdateBook_Success() throws Exception {
//        Long bookId = 1L;
//        BookUpdateRequest request = new BookUpdateRequest("Updated Title", "Updated Index", "Updated Desc",
//                "Updated Pub", "ISBN1", "updatedImage.jpg", Arrays.asList("detail1.jpg"),
//                false, 5, 800, 5.0f, "Unavailable", "Publisher1",
//                Arrays.asList("Category1"), "Author1", Arrays.asList("Tag1"));
//
//        BookUpdateResponse updateResponse = new BookUpdateResponse("Updated Title", "Updated Index",
//                "Updated Desc", "Updated Pub", "ISBN1", "updatedImage.jpg", false, 5, 800, 5.0f,
//                "Unavailable", "Publisher1", Arrays.asList("Category1"), Arrays.asList("Author1"));
//
//        when(bookService.updateBook(eq(bookId), any(BookUpdateRequest.class))).thenReturn(ResponseEntity.ok(updateResponse));
//
//        mockMvc.perform(put("/admins/books/{book-id}", bookId)
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("title", request.getTitle())
//                        .param("index", request.getIndex())
//                        .param("description", request.getDescription())
//                        .param("publication", request.getPublication())
//                        .param("isbn", request.getIsbn())
//                        .param("thumbnailImageUrl", request.getThumbnailImageUrl())
//                        .param("detailImageUrls", "detail1.jpg")
//                        .param("isPackable", String.valueOf(request.isPackable()))
//                        .param("stock", String.valueOf(request.getStock()))
//                        .param("standardPrice", String.valueOf(request.getStandardPrice()))
//                        .param("discountRate", String.valueOf(request.getDiscountRate()))
//                        .param("bookStatus", request.getBookStatus())
//                        .param("publisher", request.getPublisher())
//                        .param("categories", "Category1")
//                        .param("authors", request.getAuthors())
//                        .param("tags", "Tag1"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("admin/updateBook"))
//                .andExpect(MockMvcResultMatchers.model().attribute("success", true));
//
//        verify(bookService, times(1)).updateBook(eq(bookId), any(BookUpdateRequest.class));
//    }
//
//    @Test
//    @DisplayName("PUT /admins/books/{book-id} - Update book - Failure")
//    void testUpdateBook_Failure() throws Exception {
//        Long bookId = 1L;
//        BookUpdateRequest request = new BookUpdateRequest("Updated Title", "Updated Index", "Updated Desc",
//                "Updated Pub", "ISBN1", "updatedImage.jpg", Arrays.asList("detail1.jpg"),
//                false, 5, 800, 5.0f, "Unavailable", "Publisher1",
//                Arrays.asList("Category1"), "Author1", Arrays.asList("Tag1"));
//
//        when(bookService.updateBook(eq(bookId), any(BookUpdateRequest.class))).thenReturn(ResponseEntity.status(500).build());
//
//        mockMvc.perform(put("/admins/books/{book-id}", bookId)
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("title", request.getTitle())
//                        .param("index", request.getIndex())
//                        .param("description", request.getDescription())
//                        .param("publication", request.getPublication())
//                        .param("isbn", request.getIsbn())
//                        .param("thumbnailImageUrl", request.getThumbnailImageUrl())
//                        .param("detailImageUrls", "detail1.jpg")
//                        .param("isPackable", String.valueOf(request.isPackable()))
//                        .param("stock", String.valueOf(request.getStock()))
//                        .param("standardPrice", String.valueOf(request.getStandardPrice()))
//                        .param("discountRate", String.valueOf(request.getDiscountRate()))
//                        .param("bookStatus", request.getBookStatus())
//                        .param("publisher", request.getPublisher())
//                        .param("categories", "Category1")
//                        .param("authors", request.getAuthors())
//                        .param("tags", "Tag1"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("admin/updateBook"))
//                .andExpect(MockMvcResultMatchers.model().attribute("error", "도서 수정에 실패했습니다."))
//                .andExpect(MockMvcResultMatchers.model().attribute("bookUpdateRequest", request));
//
//        verify(bookService, times(1)).updateBook(eq(bookId), any(BookUpdateRequest.class));
//    }
//
//    @Test
//    @DisplayName("DELETE /admins/books/{book-id} - Delete book - Success")
//    void testDeleteBook_Success() throws Exception {
//        Long bookId = 1L;
//        BookDeleteResponse deleteResponse = new BookDeleteResponse("Book deleted successfully.");
//
//        when(bookService.deleteBook(bookId)).thenReturn(ResponseEntity.ok(deleteResponse));
//
//        mockMvc.perform(delete("/admins/books/{book-id}", bookId)
//                        .param("page", "0")
//                        .param("size", "10")
//                        .param("sort", "title,asc"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//                .andExpect(MockMvcResultMatchers.redirectedUrl(getRedirectUrl(0, 10, "title,asc", "/admins/books")))
//                .andExpect(MockMvcResultMatchers.flash().attribute("success", true));
//
//        verify(bookService, times(1)).deleteBook(bookId);
//    }
//
//    @Test
//    @DisplayName("DELETE /admins/books/{book-id} - Delete book - Failure")
//    void testDeleteBook_Failure() throws Exception {
//        Long bookId = 1L;
//
//        when(bookService.deleteBook(bookId)).thenReturn(ResponseEntity.status(500).build());
//
//        mockMvc.perform(delete("/admins/books/{book-id}", bookId)
//                        .param("page", "0")
//                        .param("size", "10")
//                        .param("sort", "title,asc"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//                .andExpect(MockMvcResultMatchers.redirectedUrl(getRedirectUrl(0, 10, "title,asc", "/admins/books")))
//                .andExpect(MockMvcResultMatchers.flash().attribute("error", "도서 삭제에 실패했습니다."));
//
//        verify(bookService, times(1)).deleteBook(bookId);
//    }
//}
