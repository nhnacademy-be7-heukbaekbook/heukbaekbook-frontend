package com.nhnacademy.heukbaekfrontend.book.client;

import com.nhnacademy.heukbaekfrontend.book.dto.response.BookDetailResponse;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookResponse;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookCartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "bookClient", url = "http://localhost:8082/api/books")
public interface BookClient {

    @GetMapping("/summary")
    List<BookCartResponse> getBooksSummary(@RequestParam List<Long> bookIds);

    @GetMapping("/{bookId}")
    ResponseEntity<BookDetailResponse> getBook(@PathVariable Long bookId);

    @GetMapping
    Page<BookResponse> getBooks(Pageable pageable);

    @GetMapping("/search")
    Page<BookResponse> searchBooks(@RequestParam("keyword") String keyword, Pageable pageable);
}