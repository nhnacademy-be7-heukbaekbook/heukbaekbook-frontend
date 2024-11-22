package com.nhnacademy.heukbaekfrontend.cart.service.impl;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSummaryResponse;
import com.nhnacademy.heukbaekfrontend.cart.client.CartClient;
import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateRequest;
import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateResponse;
import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
import com.nhnacademy.heukbaekfrontend.common.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final HashOperations<String, String, Integer> hashOperations;

    private final BookClient bookClient;

    private final CartClient cartClient;

    private final CommonService commonService;

    public CartServiceImpl(RedisTemplate<String, Object> redisTemplate,
                           BookClient bookClient,
                           CartClient cartClient,
                           CommonService commonService) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.bookClient = bookClient;
        this.cartClient = cartClient;
        this.commonService = commonService;
    }


    @Override
    public List<Book> getBooksFromCart(String sessionId) {
        Map<String, Integer> entries = hashOperations.entries(sessionId);
        List<Long> bookIds = entries.keySet().stream()
                .map(Long::parseLong)
                .toList();

        List<BookSummaryResponse> booksSummary = bookClient.getBooksSummary(bookIds);

        return booksSummary.stream()
                .map(bookSummaryResponse -> {
                    Integer quantity = entries.get(bookSummaryResponse.id().toString());
                    return createBook(bookSummaryResponse, quantity);
                })
                .toList();
    }

    @Override
    public List<Book> getBooksByBookIdsFromCart(String sessionId, List<Long> bookIds) {
        log.info("sessionId : {}, bookIds : {}", sessionId, bookIds);
        Map<String, Integer> entries = hashOperations.entries(sessionId);

        List<BookSummaryResponse> booksSummary = bookClient.getBooksSummary(bookIds);
        log.info("booksSummary: {}", booksSummary);

        return booksSummary.stream()
                .map(bookSummaryResponse -> {
                    Integer quantity = entries.get(bookSummaryResponse.id().toString());
                    return createBook(bookSummaryResponse, quantity);
                })
                .toList();
    }

    @Override
    public CartCreateResponse createBookToCart(String sessionId, Long bookId, int quantity) {
        String hashKey = String.valueOf(bookId);
        Object o = hashOperations.get(sessionId, hashKey);

        // 기존에 책이 장바구니에 있는지 검사
        if (o != null) {
            quantity += (Integer) o;
        }
        hashOperations.put(sessionId, hashKey, quantity);

        redisTemplate.expire(sessionId, Duration.ofDays(7));

        return new CartCreateResponse(bookId);
    }

    @Override
    public void updateBookQuantityFromCart(String sessionId, Long bookId, int quantity) {
        String productKey = String.valueOf(bookId);

        if (quantity <= 0) {
            // 수량이 0 이하인 경우, 해당 상품을 장바구니에서 제거
            hashOperations.delete(sessionId, productKey);
        } else {
            // 새로운 수량으로 업데이트
            hashOperations.put(sessionId, productKey, quantity);
        }

        redisTemplate.expire(sessionId, Duration.ofDays(7));
    }

    @Override
    public void deleteBookFromCart(String sessionId, Long bookId) {
        hashOperations.delete(sessionId, String.valueOf(bookId));
    }

    @Override
    public void synchronizeCartToDb(String sessionId) {
        Map<String, Integer> entries = hashOperations.entries(sessionId);


        List<CartCreateRequest> cartCreateRequests = entries.entrySet().stream()
                .map(entry -> new CartCreateRequest(Long.parseLong(entry.getKey()), entry.getValue()))
                .toList();

        for (CartCreateRequest cartCreateRequest : cartCreateRequests) {
            log.info("cartCreateRequest : {}", cartCreateRequest);
        }

        cartClient.synchronizeCartToDb(cartCreateRequests);
    }

    private Book createBook(BookSummaryResponse bookSummaryResponse, int quantity) {
        return new Book(
                bookSummaryResponse.id(),
                bookSummaryResponse.title(),
                bookSummaryResponse.isPackable(),
                commonService.formatPrice(bookSummaryResponse.price()),
                commonService.formatPrice(bookSummaryResponse.salePrice()),
                bookSummaryResponse.discountRate(),
                bookSummaryResponse.price().subtract(bookSummaryResponse.salePrice()),
                bookSummaryResponse.thumbnailUrl(),
                quantity,
                commonService.calculateTotalPriceAndFormat(bookSummaryResponse.salePrice(), quantity),
                commonService.calculateTotalPrice(bookSummaryResponse.salePrice(), quantity)
        );
    }
}