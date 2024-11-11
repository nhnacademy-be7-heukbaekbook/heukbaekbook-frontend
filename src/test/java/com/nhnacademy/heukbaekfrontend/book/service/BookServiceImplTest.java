package com.nhnacademy.heukbaekfrontend.book.service;

import com.nhnacademy.heukbaekfrontend.book.client.BookAdmin;
import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
import com.nhnacademy.heukbaekfrontend.book.dto.request.BookCreateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.request.BookUpdateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.response.*;
import com.nhnacademy.heukbaekfrontend.book.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookClient bookClient;

    @Mock
    private BookAdmin bookAdmin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBooks() {
        Pageable pageable = mock(Pageable.class);
        Page<BookResponse> bookPage = new PageImpl<>(List.of(
                new BookResponse(1L, "Book A", "2023-11-11", "10000", 0.1f, "thumbnailUrl", List.of(), null)
        ));

        when(bookClient.getBooks(any(Pageable.class))).thenReturn(bookPage);

        Page<BookResponse> result = bookService.getBooks(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).title()).isEqualTo("Book A");
        verify(bookClient, times(1)).getBooks(pageable);
    }

    @Test
    void testGetAllBooks() {
        Pageable pageable = mock(Pageable.class);
        Page<BookDetailResponse> bookDetailPage = new PageImpl<>(List.of(
                new BookDetailResponse(1L, "Book A", "Index A", "Description A", "2023-11-11", "ISBN001", "thumbnailUrl", List.of(), true, 10, 20000, 0.1f, "Available", "Publisher A", List.of("Category1"), List.of("Author A"), List.of("Tag1"))
        ));

        when(bookAdmin.getBooks(any(Pageable.class)))
                .thenReturn(ResponseEntity.ok(bookDetailPage));

        Page<BookDetailResponse> result = bookService.getAllBooks(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).title()).isEqualTo("Book A");
        verify(bookAdmin, times(1)).getBooks(pageable);
    }

    @Test
    void testGetBookById() {
        BookDetailResponse bookDetail = new BookDetailResponse(1L, "Book A", "Index A", "Description A", "2023-11-11", "ISBN001", "thumbnailUrl", List.of(), true, 10, 20000, 0.1f, "Available", "Publisher A", List.of("Category1"), List.of("Author A"), List.of("Tag1"));

        when(bookClient.getBook(anyLong()))
                .thenReturn(ResponseEntity.ok(bookDetail));

        BookDetailResponse result = bookService.getBookById(1L);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Book A");
        verify(bookClient, times(1)).getBook(1L);
    }

    @Test
    void testSearchBooks() {
        List<BookSearchResponse> searchResults = List.of(
                new BookSearchResponse("Book A", "coverUrl", "Description A", "Category1", "Author A", "Publisher A", null, "ISBN001", 20000, 18000)
        );

        when(bookAdmin.searchBooks(anyString())).thenReturn(searchResults);

        List<BookSearchResponse> result = bookService.searchBooks("Book A");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Book A");
        verify(bookAdmin, times(1)).searchBooks("Book A");
    }

    @Test
    void testDeleteBook() {
        BookDeleteResponse response = new BookDeleteResponse("Book deleted successfully");

        when(bookAdmin.deleteBook(anyLong()))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<BookDeleteResponse> result = bookService.deleteBook(1L);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().message()).isEqualTo("Book deleted successfully");
        verify(bookAdmin, times(1)).deleteBook(1L);
    }

    @Test
    void testRegisterBook() {
        BookCreateRequest request = new BookCreateRequest("New Book", "Index B", "Description B", "2023-12-01", "ISBN002", "imageUrl", true, 15, 25000, 0.15f, "Publisher B", "Category1", "Author A");
        BookCreateResponse response = new BookCreateResponse("New Book", "Index B", "Description B", "2023-12-01", "ISBN002", true, 15, 25000, 0.15f, "Publisher B", List.of("Category1"), List.of("Author A"));

        when(bookAdmin.registerBook(any(BookCreateRequest.class)))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<BookCreateResponse> result = bookService.registerBook(request);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().title()).isEqualTo("New Book");
        verify(bookAdmin, times(1)).registerBook(request);
    }

    @Test
    void testUpdateBook() {
        BookUpdateRequest request = new BookUpdateRequest("Updated Book", "Index C", "Updated Description", "2023-12-15", "ISBN003", "thumbnailUrl", List.of("detailUrl1"), true, 20, 30000, 0.2f, "Available", "Publisher C", List.of("Category2"), "Author B", List.of("Tag2"));
        BookUpdateResponse response = new BookUpdateResponse("Updated Book", "Index C", "Updated Description", "2023-12-15", "ISBN003", "imageUrl", true, 20, 30000, 0.2f, "Available", "Publisher C", List.of("Category2"), List.of("Author B"));

        when(bookAdmin.updateBook(anyLong(), any(BookUpdateRequest.class)))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<BookUpdateResponse> result = bookService.updateBook(1L, request);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().title()).isEqualTo("Updated Book");
        verify(bookAdmin, times(1)).updateBook(1L, request);
    }
}
