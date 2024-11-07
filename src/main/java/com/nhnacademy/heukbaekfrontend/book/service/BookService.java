package com.nhnacademy.heukbaekfrontend.book.service;

import com.nhnacademy.heukbaekfrontend.book.client.BookAdmin;
import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
import com.nhnacademy.heukbaekfrontend.book.dto.request.BookCreateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.request.BookUpdateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.response.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookAdmin bookAdmin;
    private final BookClient bookClient;

    @Autowired
    public BookService(BookAdmin bookAdmin, BookClient bookClient) {
        this.bookAdmin = bookAdmin;
        this.bookClient = bookClient;
    }

    public Page<BookDetailResponse> getAllBooks(Pageable pageable) {
        return bookAdmin.getBooks(pageable).getBody();
    }

    public BookDetailResponse getBookById(Long bookId) {
        ResponseEntity<BookDetailResponse> response = bookClient.getBook(bookId);
        return response.getBody();
    }

    public List<BookSearchResponse> searchBooks(String title) {
        return bookAdmin.searchBooks(title);
    }

    public ResponseEntity<BookDeleteResponse> deleteBook(Long bookId) {
        return bookAdmin.deleteBook(bookId);
    }

    public ResponseEntity<BookCreateResponse> registerBook(BookCreateRequest request) {
        return bookAdmin.registerBook(request);
    }

    public ResponseEntity<BookUpdateResponse> updateBook(Long bookId, BookUpdateRequest request) {
        return bookAdmin.updateBook(bookId, request);
    }
}
