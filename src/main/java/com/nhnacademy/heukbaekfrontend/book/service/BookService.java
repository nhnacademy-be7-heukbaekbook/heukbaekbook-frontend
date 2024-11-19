package com.nhnacademy.heukbaekfrontend.book.service;

import com.nhnacademy.heukbaekfrontend.book.dto.request.BookCreateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.request.BookUpdateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.response.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookService {
    Page<BookResponse> getBooks(Pageable pageable);

    Page<BookDetailResponse> getAllBooks(Pageable pageable);

    BookDetailResponse getBookById(Long bookId);

    List<BookSearchResponse> searchBooks(String title);

    ResponseEntity<BookDeleteResponse> deleteBook(Long bookId);

    ResponseEntity<BookCreateResponse> registerBook(BookCreateRequest request);

    ResponseEntity<BookUpdateResponse> updateBook(Long bookId, BookUpdateRequest request);

    Page<BookResponse> getBooksByCategoryId(Long categoryId, Pageable pageable);

    BookViewResponse getBookDetailByBookId(Long bookId);
}
