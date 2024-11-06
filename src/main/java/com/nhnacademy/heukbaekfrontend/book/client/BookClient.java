package com.nhnacademy.heukbaekfrontend.book.client;

import com.nhnacademy.heukbaekfrontend.book.dto.response.BookDetailResponse;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSummaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "bookClient", url = "http://localhost:8084/api/books")
public interface BookClient {

    @GetMapping("/summary")
    List<BookSummaryResponse> getBooksSummary(@RequestParam List<Long> bookIds);

    @GetMapping("/{bookId}")
    ResponseEntity<BookDetailResponse> getBook(@PathVariable Long bookId);
}