package com.nhnacademy.heukbaekfrontend.cart.service.impl;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.book.dto.redis.BookInfo;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSummaryResponse;
import com.nhnacademy.heukbaekfrontend.book.service.RedisBookService;
import com.nhnacademy.heukbaekfrontend.cart.client.CartClient;
import com.nhnacademy.heukbaekfrontend.cart.dto.*;
import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
import com.nhnacademy.heukbaekfrontend.common.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final HashOperations<String, String, Integer> hashOperations;

    private final RedisBookService redisBookService;

    private final CartClient cartClient;

    private final BookClient bookClient;

    private final CommonService commonService;

    public CartServiceImpl(RedisTemplate<String, Object> redisTemplate,
                           RedisBookService redisBookService,
                           CartClient cartClient,
                           BookClient bookClient,
                           CommonService commonService) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.redisBookService = redisBookService;
        this.cartClient = cartClient;
        this.bookClient = bookClient;
        this.commonService = commonService;
    }


    @Override
    public CartResponse getBooksFromCart(String sessionId) {
        Map<String, Integer> entries = hashOperations.entries(sessionId);
        List<Long> bookIds = entries.keySet().stream()
                .map(Long::parseLong)
                .toList();

        log.info("bookIds : {}", bookIds);

        List<BookInfo> bookInfos = redisBookService.getBookInfos(bookIds);

        List<CartBookResponse> cartBookResponses = bookInfos.stream()
                .map(bookInfo -> {
                    Integer quantity = entries.get(bookInfo.id().toString());
                    return createCartBookResponse(bookInfo, quantity);
                })
                .toList();

        return new CartResponse(cartBookResponses);
    }

    @Override
    public List<Book> getBooksByBookIdsFromCart(String sessionId, List<Long> bookIds) {
        log.info("sessionId : {}, bookIds : {}", sessionId, bookIds);
        Map<String, Integer> entries = hashOperations.entries(sessionId);

        List<BookSummaryResponse> booksSummary = bookClient.getBooksSummary(bookIds);

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

        if (entries.isEmpty()) {
            log.info("No cart data found for sessionId: {}", sessionId);
            return;
        }

        List<CartCreateRequest> cartCreateRequests = entries.entrySet().stream()
                .map(entry -> new CartCreateRequest(Long.parseLong(entry.getKey()), entry.getValue()))
                .toList();

        for (CartCreateRequest cartCreateRequest : cartCreateRequests) {
            log.info("cartCreateRequest : {}", cartCreateRequest);
        }

        cartClient.synchronizeCartToDb(cartCreateRequests);
        redisTemplate.delete(sessionId);
    }

    @Override
    public void synchronizeCartFromDb(String sessionId) {
        List<CartBookSummaryResponse> cartBookSummaryResponses = cartClient.synchronizeCartFromDb();

        if (cartBookSummaryResponses.isEmpty()) {
            return;
        }

        for (CartBookSummaryResponse cartBookSummaryResponse : cartBookSummaryResponses) {
            String bookId = cartBookSummaryResponse.bookId().toString();
            hashOperations.put(sessionId, bookId, cartBookSummaryResponse.quantity());
        }
    }


    private CartBookResponse createCartBookResponse(BookInfo bookInfo, int quantity) {
        return new CartBookResponse(
                bookInfo.id(),
                bookInfo.thumbnailUrl(),
                bookInfo.title(),
                bookInfo.price(),
                bookInfo.salePrice(),
                bookInfo.discountRate(),
                quantity
        );
    }

    private Book createBook(BookSummaryResponse bookSummaryResponse, int quantity) {
        return new Book(
                bookSummaryResponse.id(),
                bookSummaryResponse.title(),
                bookSummaryResponse.isPackable(),
                bookSummaryResponse.wrappingPaperId(),
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