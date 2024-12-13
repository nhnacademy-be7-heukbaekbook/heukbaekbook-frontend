package com.nhnacademy.heukbaekfrontend.book.service;

import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
import com.nhnacademy.heukbaekfrontend.book.dto.redis.BookInfo;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSummaryResponse;
import com.nhnacademy.heukbaekfrontend.book.service.impl.RedisBookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RedisBookServiceImplTest {

    private RedisBookServiceImpl redisBookService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private BookClient bookClient;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        redisBookService = new RedisBookServiceImpl(redisTemplate, bookClient);
    }

    @Test
    void testCreateBooks() {
        List<BookInfo> bookInfos = List.of(
                new BookInfo(1L, "url1", "title1", BigDecimal.valueOf(100), BigDecimal.valueOf(90), BigDecimal.valueOf(10)),
                new BookInfo(2L, "url2", "title2", BigDecimal.valueOf(200), BigDecimal.valueOf(180), BigDecimal.valueOf(10))
        );

        redisBookService.createBooks(bookInfos);

        for (BookInfo bookInfo : bookInfos) {
            verify(hashOperations).put(String.valueOf(bookInfo.id()), "thumbnailUrl", bookInfo.thumbnailUrl());
            verify(hashOperations).put(String.valueOf(bookInfo.id()), "title", bookInfo.title());
            verify(hashOperations).put(String.valueOf(bookInfo.id()), "price", bookInfo.price());
            verify(hashOperations).put(String.valueOf(bookInfo.id()), "salePrice", bookInfo.salePrice());
            verify(hashOperations).put(String.valueOf(bookInfo.id()), "discountRate", bookInfo.discountRate());
            verify(redisTemplate).expire(String.valueOf(bookInfo.id()), Duration.ofMinutes(30));
        }
    }

    @Test
    void testGetBookInfos_WhenAllBooksInRedis() {
        List<Long> bookIds = List.of(1L, 2L);

        when(hashOperations.entries("1")).thenReturn(Map.of(
                "thumbnailUrl", "url1",
                "title", "title1",
                "price", BigDecimal.valueOf(100),
                "salePrice", BigDecimal.valueOf(90),
                "discountRate", BigDecimal.valueOf(10)
        ));
        when(hashOperations.entries("2")).thenReturn(Map.of(
                "thumbnailUrl", "url2",
                "title", "title2",
                "price", BigDecimal.valueOf(200),
                "salePrice", BigDecimal.valueOf(180),
                "discountRate", BigDecimal.valueOf(10)
        ));

        List<BookInfo> bookInfos = redisBookService.getBookInfos(bookIds);

        assertThat(bookInfos).hasSize(2);
        assertThat(bookInfos).extracting(BookInfo::id).containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void testGetBookInfos_WhenSomeBooksMissingInRedis() {
        List<Long> bookIds = List.of(1L, 2L);

        when(hashOperations.entries("1")).thenReturn(Map.of(
                "thumbnailUrl", "url1",
                "title", "title1",
                "price", BigDecimal.valueOf(100),
                "salePrice", BigDecimal.valueOf(90),
                "discountRate", BigDecimal.valueOf(10)
        ));
        when(hashOperations.entries("2")).thenReturn(Collections.emptyMap());

        when(bookClient.getBooksSummary(List.of(2L))).thenReturn(List.of(
                new BookSummaryResponse(2L, "title2", true, null, BigDecimal.valueOf(200), BigDecimal.valueOf(180), BigDecimal.valueOf(10), "url2")
        ));

        List<BookInfo> bookInfos = redisBookService.getBookInfos(bookIds);

        assertThat(bookInfos).hasSize(2);
        assertThat(bookInfos).extracting(BookInfo::id).containsExactlyInAnyOrder(1L, 2L);
        verify(hashOperations, times(1)).put("2", "thumbnailUrl", "url2");
    }

    @Test
    void testMapToBookInfo() {
        Map<Object, Object> bookData = Map.of(
                "thumbnailUrl", "url1",
                "title", "title1",
                "price", BigDecimal.valueOf(100),
                "salePrice", BigDecimal.valueOf(90),
                "discountRate", BigDecimal.valueOf(10)
        );

        BookInfo bookInfo = mapToBookInfo(1L, bookData);

        assertThat(bookInfo).isNotNull();
        assertThat(bookInfo.id()).isEqualTo(1L);
        assertThat(bookInfo.title()).isEqualTo("title1");
        assertThat(bookInfo.thumbnailUrl()).isEqualTo("url1");
        assertThat(bookInfo.price()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(bookInfo.salePrice()).isEqualTo(BigDecimal.valueOf(90));
        assertThat(bookInfo.discountRate()).isEqualTo(BigDecimal.valueOf(10));
    }

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
