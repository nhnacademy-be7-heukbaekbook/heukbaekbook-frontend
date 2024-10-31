package com.nhnacademy.heukbaekfrontend.book.client;

import com.nhnacademy.heukbaekfrontend.book.dto.BookSummaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "bookClient", url = "http://localhost:8082/api/books")
public interface BookClient {

    @GetMapping("/summary")
    List<BookSummaryResponse> getBooksSummary(@RequestParam List<Long> bookIds);
}