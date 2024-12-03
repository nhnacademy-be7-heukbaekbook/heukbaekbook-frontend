package com.nhnacademy.heukbaekfrontend.book.service.impl;


import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
import com.nhnacademy.heukbaekfrontend.book.dto.redis.BookInfo;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSummaryResponse;
import com.nhnacademy.heukbaekfrontend.book.service.RedisBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisBookServiceImpl implements RedisBookService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final BookClient bookClient;

    @Override
    public void createBooks(List<BookInfo> bookInfos) {
        for (BookInfo bookInfo : bookInfos) {
            String key = String.valueOf(bookInfo.id());

            // Redis에 책 정보 저장
            redisTemplate.opsForHash().put(key, "thumbnailUrl", bookInfo.thumbnailUrl());
            redisTemplate.opsForHash().put(key, "title", bookInfo.title());
            redisTemplate.opsForHash().put(key, "price", bookInfo.price());
            redisTemplate.opsForHash().put(key, "salePrice", bookInfo.salePrice());
            redisTemplate.opsForHash().put(key, "discountRate", bookInfo.discountRate());

            redisTemplate.expire(key, Duration.ofMinutes(30));
        }
    }

    @Override
    public List<BookInfo> getBookInfos(List<Long> bookIds) {
        // Step 1: Redis에서 데이터 조회
        List<Long> missingBookIds = new ArrayList<>();
        List<BookInfo> bookInfos = fetchBooksFromRedis(bookIds, missingBookIds);

        // Step 2: 데이터베이스 또는 외부 API에서 누락된 데이터 조회
        if (!missingBookIds.isEmpty()) {
            List<BookInfo> dbBooks = fetchBooksFromDatabase(missingBookIds);
            createBooks(dbBooks); // Redis에 캐싱
            bookInfos.addAll(dbBooks);
        }

        return bookInfos;
    }

    // Redis에서 책 정보 조회
    private List<BookInfo> fetchBooksFromRedis(List<Long> bookIds, List<Long> missingBookIds) {
        return bookIds.stream()
                .map(bookId -> {
                    String key = String.valueOf(bookId);
                    Map<Object, Object> book = redisTemplate.opsForHash().entries(key);

                    if (book.isEmpty()) {
                        missingBookIds.add(bookId);
                        return null; // Redis에 데이터가 없음을 표시
                    }

                    return mapToBookInfo(bookId, book);
                })
                .filter(Objects::nonNull) // null 제거
                .collect(Collectors.toList());
    }

    // 데이터베이스 또는 외부 API에서 책 정보 조회
    private List<BookInfo> fetchBooksFromDatabase(List<Long> bookIds) {
        List<BookSummaryResponse> booksSummary = bookClient.getBooksSummary(bookIds);

        return booksSummary.stream()
                .map(bookSummary -> new BookInfo(
                        bookSummary.id(),
                        bookSummary.thumbnailUrl(),
                        bookSummary.title(),
                        bookSummary.price(),
                        bookSummary.salePrice(),
                        bookSummary.discountRate()
                ))
                .collect(Collectors.toList());
    }

    // Redis 데이터에서 BookInfo 생성
    private BookInfo mapToBookInfo(Long bookId, Map<Object, Object> book) {
        return new BookInfo(
                bookId,
                (String) book.get("thumbnailUrl"),
                (String) book.get("title"),
                (BigDecimal) book.get("price"), // 데이터 형 변환
                (BigDecimal) book.get("salePrice"),
                (BigDecimal) book.get("discountRate")
        );
    }
}
