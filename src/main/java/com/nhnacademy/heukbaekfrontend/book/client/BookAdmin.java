package com.nhnacademy.heukbaekfrontend.book.client;

import com.nhnacademy.heukbaekfrontend.book.dto.request.BookUpdateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.response.*;
import com.nhnacademy.heukbaekfrontend.book.dto.request.BookCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "bookAdmin", url = "http://localhost:8082")
public interface BookAdmin {

    @PostMapping("/api/admin/aladin")
    List<BookSearchResponse> searchBooks(@RequestParam("title") String title);

    @PostMapping("/api/admin/books")
    ResponseEntity<BookCreateResponse> registerBook(@RequestBody BookCreateRequest request);

    @GetMapping("/api/admin/books")
    ResponseEntity<Page<BookDetailResponse>> getBooks(Pageable pageable);

    @PutMapping("/api/admin/books/{bookId}")
    ResponseEntity<BookUpdateResponse> updateBook(@PathVariable Long bookId, @RequestBody BookUpdateRequest request);

    @DeleteMapping("/api/admin/books/{bookId}")
    ResponseEntity<BookDeleteResponse> deleteBook(@PathVariable Long bookId);

}
