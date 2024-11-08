//package com.nhnacademy.heukbaekfrontend.book.service;
//
//import com.nhnacademy.heukbaekfrontend.book.client.BookAdmin;
//import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
//import com.nhnacademy.heukbaekfrontend.book.dto.request.BookCreateRequest;
//import com.nhnacademy.heukbaekfrontend.book.dto.request.BookUpdateRequest;
//import com.nhnacademy.heukbaekfrontend.book.dto.response.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.data.domain.*;
//
//import org.springframework.http.ResponseEntity;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class BookServiceTest {
//
//    @Mock
//    private BookAdmin bookAdmin;
//
//    @Mock
//    private BookClient bookClient;
//
//    @InjectMocks
//    private BookService bookService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    @DisplayName("Test getAllBooks - Success")
//    void testGetAllBooks_Success() {
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());
//        List<BookDetailResponse> bookList = Arrays.asList(
//                new BookDetailResponse(1L, "Title1", "Index1", "Desc1", "Pub1", "ISBN1",
//                        "thumb1.jpg", Arrays.asList("detail1.jpg"), true, 10, 1000, 10.0f,
//                        "Available", "Publisher1", Arrays.asList("Category1"), Arrays.asList("Author1"), Arrays.asList("Tag1"))
//        );
//        Page<BookDetailResponse> bookPage = new PageImpl<>(bookList, pageable, bookList.size());
//
//        when(bookAdmin.getBooks(pageable)).thenReturn(ResponseEntity.ok(bookPage));
//
//        Page<BookDetailResponse> result = bookService.getAllBooks(pageable);
//
//        assertNotNull(result);
//        assertEquals(1, result.getTotalElements());
//        verify(bookAdmin, times(1)).getBooks(pageable);
//    }
//
//    @Test
//    @DisplayName("Test getBookById - Success")
//    void testGetBookById_Success() {
//        Long bookId = 1L;
//        BookDetailResponse bookDetail = new BookDetailResponse(bookId, "Title1", "Index1", "Desc1", "Pub1",
//                "ISBN1", "thumb1.jpg", Arrays.asList("detail1.jpg"), true, 10, 1000, 10.0f,
//                "Available", "Publisher1", Arrays.asList("Category1"), Arrays.asList("Author1"), Arrays.asList("Tag1"));
//
//        when(bookClient.getBook(bookId)).thenReturn(ResponseEntity.ok(bookDetail));
//
//        BookDetailResponse result = bookService.getBookById(bookId);
//
//        assertNotNull(result);
//        assertEquals("Title1", result.title());
//        verify(bookClient, times(1)).getBook(bookId);
//    }
//
//    @Test
//    @DisplayName("Test searchBooks - Success")
//    void testSearchBooks_Success() {
//        String title = "Spring";
//        List<BookSearchResponse> searchResults = Arrays.asList(
//                new BookSearchResponse("Spring in Action", "cover1.jpg", "A comprehensive guide to Spring.",
//                        "Programming", "Craig Walls", "Publisher1", null, "ISBN123456", 5000, 4500)
//        );
//
//        when(bookAdmin.searchBooks(title)).thenReturn(searchResults);
//
//        List<BookSearchResponse> result = bookService.searchBooks(title);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("Spring in Action", result.get(0).title());
//        verify(bookAdmin, times(1)).searchBooks(title);
//    }
//
//    @Test
//    @DisplayName("Test deleteBook - Success")
//    void testDeleteBook_Success() {
//        Long bookId = 1L;
//        BookDeleteResponse deleteResponse = new BookDeleteResponse("Book deleted successfully.");
//
//        when(bookAdmin.deleteBook(bookId)).thenReturn(ResponseEntity.ok(deleteResponse));
//
//        ResponseEntity<BookDeleteResponse> response = bookService.deleteBook(bookId);
//
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("Book deleted successfully.", response.getBody().message());
//        verify(bookAdmin, times(1)).deleteBook(bookId);
//    }
//
//    @Test
//    @DisplayName("Test registerBook - Success")
//    void testRegisterBook_Success() {
//        BookCreateRequest request = new BookCreateRequest("Title1", "Index1", "Desc1", "Pub1",
//                "ISBN1", "image1.jpg", true, 10, 1000, 10.0f, "Publisher1", "Category1", "Author1");
//
//        BookCreateResponse createResponse = new BookCreateResponse("Title1", "Index1", "Desc1", "Pub1",
//                "ISBN1", true, 10, 1000, 10.0f, "Publisher1",
//                Arrays.asList("Category1"), Arrays.asList("Author1"));
//
//        when(bookAdmin.registerBook(request)).thenReturn(ResponseEntity.ok(createResponse));
//
//        ResponseEntity<BookCreateResponse> response = bookService.registerBook(request);
//
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("Title1", response.getBody().title());
//        verify(bookAdmin, times(1)).registerBook(request);
//    }
//
//    @Test
//    @DisplayName("Test updateBook - Success")
//    void testUpdateBook_Success() {
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
//        when(bookAdmin.updateBook(bookId, request)).thenReturn(ResponseEntity.ok(updateResponse));
//
//        ResponseEntity<BookUpdateResponse> response = bookService.updateBook(bookId, request);
//
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("Updated Title", response.getBody().title());
//        verify(bookAdmin, times(1)).updateBook(bookId, request);
//    }
//}
