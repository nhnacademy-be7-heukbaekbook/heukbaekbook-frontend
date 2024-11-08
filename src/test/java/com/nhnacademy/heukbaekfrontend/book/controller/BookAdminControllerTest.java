package com.nhnacademy.heukbaekfrontend.book.controller;

import com.nhnacademy.heukbaekfrontend.book.dto.request.BookCreateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.request.BookUpdateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookDetailResponse;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSearchResponse;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookAdminControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private BookAdminController bookAdminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test viewAllBooks - Success")
    void testViewAllBooks_Success() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());
        List<BookDetailResponse> bookList = Arrays.asList(
                new BookDetailResponse(1L, "Title1", "Index1", "Desc1", "Pub1", "ISBN1",
                        "thumb1.jpg", Arrays.asList("detail1.jpg"), true, 10, 1000, 10.0f,
                        "Available", "Publisher1", Arrays.asList("Category1"), Arrays.asList("Author1"), Arrays.asList("Tag1"))
        );
        Page<BookDetailResponse> bookPage = new PageImpl<>(bookList, pageable, bookList.size());

        when(bookService.getAllBooks(pageable)).thenReturn(bookPage);

        String viewName = bookAdminController.viewAllBooks(pageable, model);

        assertEquals("admin/viewAllBooks", viewName);
        verify(model, times(1)).addAttribute("books", bookPage);
        verify(model, times(1)).addAttribute("page", pageable.getPageNumber());
        verify(model, times(1)).addAttribute("size", pageable.getPageSize());
        verify(model, times(1)).addAttribute("sort", pageable.getSort().toString());
    }

    @Test
    @DisplayName("Test searchBooks - Success")
    void testSearchBooks_Success() {
        String title = "Spring";
        List<BookSearchResponse> searchResults = Arrays.asList(
                new BookSearchResponse("Spring in Action", "cover1.jpg", "A comprehensive guide to Spring.",
                        "Programming", "Craig Walls", "Publisher1", null, "ISBN123456", 5000, 4500)
        );

        when(bookService.searchBooks(title)).thenReturn(searchResults);

        String viewName = bookAdminController.searchBooks(title, model);

        assertEquals("admin/searchBookFromAladin", viewName);
        verify(model, times(1)).addAttribute("responses", searchResults);
    }

    @Test
    @DisplayName("Test registerBook - Success")
    void testRegisterBook_Success() {
        BookCreateRequest request = new BookCreateRequest("Title1", "Index1", "Desc1", "Pub1", "ISBN1", "image1.jpg", true, 10, 1000, 10.0f, "Publisher1", "Category1", "Author1");

        String viewName = bookAdminController.selectBookForRegistration(request, model);

        assertEquals("admin/registerBook", viewName);
        verify(model, times(1)).addAttribute("bookCreateRequest", request);
    }
}

class BookControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private Model model;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test viewBook - Success")
    void testViewBook_Success() {
        Long bookId = 1L;
        BookDetailResponse bookDetail = new BookDetailResponse(bookId, "Title1", "Index1", "Desc1", "Pub1",
                "ISBN1", "thumb1.jpg", Arrays.asList("detail1.jpg"), true, 10, 1000, 10.0f,
                "Available", "Publisher1", Arrays.asList("Category1"), Arrays.asList("Author1"), Arrays.asList("Tag1"));

        when(bookService.getBookById(bookId)).thenReturn(bookDetail);

        String viewName = bookController.viewBook(bookId, model);

        assertEquals("admin/bookDetail", viewName);
        verify(model, times(1)).addAttribute("book", bookDetail);
    }
}
