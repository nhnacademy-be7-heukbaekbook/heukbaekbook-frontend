package com.nhnacademy.heukbaekfrontend.cart.service.impl;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSummaryResponse;
import com.nhnacademy.heukbaekfrontend.cart.client.CartClient;
import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateRequest;
import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateResponse;
import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
import com.nhnacademy.heukbaekfrontend.common.service.CommonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    private CartService cartService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, String, Object> hashOperations;

    @Mock
    private BookClient bookClient;

    @Mock
    private CartClient cartClient;

    @Mock
    private CommonService commonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 명시적으로 doReturn 사용
        doReturn(hashOperations).when(redisTemplate).opsForHash();

        cartService = new CartServiceImpl(redisTemplate, bookClient, cartClient, commonService);
    }

//    @Test
//    void testGetBooksFromCart() {
//        String sessionId = "mockSessionId";
//        Map<String, Object> mockEntries = Map.of("1", 2, "2", 1);
//        List<BookSummaryResponse> mockBooks = List.of(
//                new BookSummaryResponse(1L, "Book A", true, 101L, BigDecimal.valueOf(10000),
//                        BigDecimal.valueOf(9000), BigDecimal.valueOf(0.1), "http://example.com/bookA.jpg"),
//                new BookSummaryResponse(2L, "Book B", false, null, BigDecimal.valueOf(20000),
//                        BigDecimal.valueOf(18000), BigDecimal.valueOf(0.1), "http://example.com/bookB.jpg")
//        );
//
//        when(hashOperations.entries(sessionId)).thenReturn(mockEntries);
//        when(bookClient.getBooksSummary(List.of(1L, 2L))).thenReturn(mockBooks);
//        when(commonService.formatPrice(any())).thenReturn("10,000", "9,000", "20,000", "18,000");
//        when(commonService.calculateTotalPriceAndFormat(any(), anyInt())).thenReturn("18,000", "18,000");
//        when(commonService.calculateTotalPrice(any(), anyInt())).thenReturn(BigDecimal.valueOf(18000));
//
//        List<Book> books = cartService.getBooksFromCart(sessionId);
//
//        assertThat(books).hasSize(2);
//        assertThat(books.get(0).title()).isEqualTo("Book A");
//        assertThat(books.get(1).title()).isEqualTo("Book B");
//        verify(hashOperations).entries(sessionId);
//        verify(bookClient).getBooksSummary(List.of(1L, 2L));
//    }

    @Test
    void testCreateBookToCart() {
        String sessionId = "mockSessionId";
        Long bookId = 1L;
        int initialQuantity = 2;

        when(hashOperations.get(sessionId, String.valueOf(bookId))).thenReturn(null);
        CartCreateResponse response = cartService.createBookToCart(sessionId, bookId, initialQuantity);

        assertThat(response.bookId()).isEqualTo(bookId);
        verify(hashOperations).put(sessionId, String.valueOf(bookId), initialQuantity);
        verify(redisTemplate).expire(eq(sessionId), any());
    }

    @Test
    void testUpdateBookQuantityFromCart() {
        String sessionId = "mockSessionId";
        Long bookId = 1L;
        int newQuantity = 3;

        cartService.updateBookQuantityFromCart(sessionId, bookId, newQuantity);

        verify(hashOperations).put(sessionId, String.valueOf(bookId), newQuantity);
        verify(redisTemplate).expire(eq(sessionId), any());
    }

    @Test
    void testDeleteBookFromCart() {
        String sessionId = "mockSessionId";
        Long bookId = 1L;

        cartService.deleteBookFromCart(sessionId, bookId);

        verify(hashOperations).delete(sessionId, String.valueOf(bookId));
    }

//    @Test
//    void testSynchronizeCartToDb() {
//        String sessionId = "mockSessionId";
//        Map<String, Object> mockEntries = Map.of("1", 2, "2", 1);
//        List<CartCreateRequest> mockRequests = List.of(
//                new CartCreateRequest(1L, 2),
//                new CartCreateRequest(2L, 1)
//        );
//
//        when(hashOperations.entries(sessionId)).thenReturn(mockEntries);
//
//        cartService.synchronizeCartToDb(sessionId);
//
//        verify(cartClient).synchronizeCartToDb(mockRequests);
//    }
}
