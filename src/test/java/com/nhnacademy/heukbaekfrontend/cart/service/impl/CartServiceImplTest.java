//package com.nhnacademy.heukbaekfrontend.cart.service.impl;
//
//import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
//import com.nhnacademy.heukbaekfrontend.book.domain.Book;
//import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSummaryResponse;
//import com.nhnacademy.heukbaekfrontend.cart.client.CartClient;
//import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateRequest;
//import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateResponse;
//import com.nhnacademy.heukbaekfrontend.common.service.CommonService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.math.BigDecimal;
//import java.time.Duration;
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class CartServiceImplTest {
//
//    @InjectMocks
//    private CartServiceImpl cartService;
//
//    @Mock
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @Mock
//    private HashOperations<String, String, Integer> hashOperations;
//
//    @Mock
//    private BookClient bookClient;
//
//    @Mock
//    private CartClient cartClient;
//
//    @Mock
//    private CommonService commonService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        doAnswer(invocation -> hashOperations).when(redisTemplate).opsForHash();
//    }
//
//
//    @Test
//    void testGetBooksFromCart() {
//        // Given
//        String sessionId = "session123";
//        Map<String, Integer> mockCart = Map.of("1", 2, "2", 3);
//
//        List<BookSummaryResponse> mockBooks = List.of(
//                new BookSummaryResponse(1L, "Book 1", BigDecimal.valueOf(10000), BigDecimal.valueOf(8000), 0.2f, "thumbnailUrl1"),
//                new BookSummaryResponse(2L, "Book 2", BigDecimal.valueOf(20000), BigDecimal.valueOf(15000), 0.25f, "thumbnailUrl2")
//        );
//
//        when(hashOperations.entries(sessionId)).thenReturn(mockCart);
//        when(bookClient.getBooksSummary(anyList())).thenReturn(mockBooks);
//        when(commonService.formatPrice(any(BigDecimal.class))).thenReturn("formattedPrice");
//        when(commonService.calculateTotalPriceAndFormat(any(BigDecimal.class), anyInt())).thenReturn("totalFormattedPrice");
//        when(commonService.calculateTotalPrice(any(BigDecimal.class), anyInt())).thenReturn(BigDecimal.valueOf(16000));
//
//        // When
//        List<Book> result = cartService.getBooksFromCart(sessionId);
//
//        // Then
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).title()).isEqualTo("Book 1");
//        assertThat(result.get(1).title()).isEqualTo("Book 2");
//
//        verify(hashOperations, times(1)).entries(sessionId);
//        verify(bookClient, times(1)).getBooksSummary(anyList());
//    }
//
//    @Test
//    void testCreateBookToCart() {
//        // Given
//        String sessionId = "session123";
//        Long bookId = 1L;
//        int quantity = 2;
//
//        when(hashOperations.get(sessionId, String.valueOf(bookId))).thenReturn(null);
//
//        // When
//        CartCreateResponse response = cartService.createBookToCart(sessionId, bookId, quantity);
//
//        // Then
//        assertThat(response).isNotNull();
//        assertThat(response.bookId()).isEqualTo(bookId);
//
//        verify(hashOperations, times(1)).put(sessionId, String.valueOf(bookId), quantity);
//        verify(redisTemplate, times(1)).expire(sessionId, Duration.ofDays(7));
//    }
//
//    @Test
//    void testUpdateBookQuantityFromCart() {
//        // Given
//        String sessionId = "session123";
//        Long bookId = 1L;
//        int quantity = 0;
//
//        // When
//        cartService.updateBookQuantityFromCart(sessionId, bookId, quantity);
//
//        // Then
//        verify(hashOperations, times(1)).delete(sessionId, String.valueOf(bookId));
//        verify(redisTemplate, times(1)).expire(sessionId, Duration.ofDays(7));
//    }
//
//    @Test
//    void testDeleteBookFromCart() {
//        // Given
//        String sessionId = "session123";
//        Long bookId = 1L;
//
//        // When
//        cartService.deleteBookFromCart(sessionId, bookId);
//
//        // Then
//        verify(hashOperations, times(1)).delete(sessionId, String.valueOf(bookId));
//    }
//
//    @Test
//    void testSynchronizeCartToDb() {
//        // Given
//        String sessionId = "session123";
//        Map<String, Integer> mockCart = Map.of("1", 2, "2", 3);
//
//        when(hashOperations.entries(sessionId)).thenReturn(mockCart);
//
//        // When
//        cartService.synchronizeCartToDb(sessionId);
//
//        // Then
//        verify(hashOperations, times(1)).entries(sessionId);
//        verify(cartClient, times(1)).synchronizeCartToDb(anyList());
//    }
//}