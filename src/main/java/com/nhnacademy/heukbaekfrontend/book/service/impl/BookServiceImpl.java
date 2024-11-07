package com.nhnacademy.heukbaekfrontend.book.service.impl;

import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookResponse;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookClient bookClient;

    @Override
    public Page<BookResponse> getBooks(Pageable pageable) {
        return bookClient.getBooks(pageable);
    }
}
