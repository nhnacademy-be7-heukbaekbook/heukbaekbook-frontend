package com.nhnacademy.heukbaekfrontend.book.service.impl;

import com.nhnacademy.heukbaekfrontend.book.client.BookAdmin;
import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
import com.nhnacademy.heukbaekfrontend.book.dto.request.BookCreateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.request.BookUpdateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.response.*;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookClient bookClient;
    private final BookAdmin bookAdmin;

    @Override
    public Page<BookResponse> getBooks(Pageable pageable) {
        return bookClient.getBooks(pageable);
    }

    @Override
    public Page<BookDetailResponse> getAllBooks(Pageable pageable) {
        return bookAdmin.getBooks(pageable).getBody();
    }

    @Override
    public BookDetailResponse getBookById(Long bookId) {
        return bookClient.getBook(bookId).getBody();
    }

    @Override
    public List<BookSearchResponse> searchBooks(String title) {
        return bookAdmin.searchBooks(title);
    }

    @Override
    public ResponseEntity<BookDeleteResponse> deleteBook(Long bookId) {
        return bookAdmin.deleteBook(bookId);
    }

    @Override
    public ResponseEntity<BookCreateResponse> registerBook(BookCreateRequest request) {
        return bookAdmin.registerBook(request);
    }

    @Override
    public ResponseEntity<BookUpdateResponse> updateBook(Long bookId, BookUpdateRequest request) {
        return bookAdmin.updateBook(bookId, request);
    }
    @Override
    public Page<BookResponse> searchBooksWithPagination(String keyword, Pageable pageable) {
        return bookClient.searchBooks(keyword, pageable); // 새 메서드에서 title과 pageable 전달
    }
}
