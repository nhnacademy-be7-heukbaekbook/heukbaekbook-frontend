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
        String modifiedImageUrl = request.imageUrl() != null ?
                request.imageUrl().replace("coversum", "cover500") : null;

        BookCreateRequest modifiedRequest = new BookCreateRequest(
                request.title(),
                request.index(),
                request.description(),
                request.publishedAt(),
                request.isbn(),
                modifiedImageUrl,
                request.isPackable(),
                request.stock(),
                request.standardPrice(),
                request.discountRate(),
                request.publisher(),
                request.categories(),
                request.authors()
        );

        return bookAdmin.registerBook(modifiedRequest);
    }

    @Override
    public ResponseEntity<BookUpdateResponse> updateBook(Long bookId, BookUpdateRequest request) {
        return bookAdmin.updateBook(bookId, request);
    }

    @Override
    public Page<BookResponse> getBooksByCategoryId(Long categoryId, Pageable pageable) {
        return bookClient.getBooksByCategoryId(categoryId, pageable);
    }

    @Override
    public BookViewResponse getBookDetailByBookId(Long bookId) {
        return bookClient.getBookDetailByBookId(bookId);
    }
}
