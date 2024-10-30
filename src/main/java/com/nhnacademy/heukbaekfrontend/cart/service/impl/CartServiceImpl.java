package com.nhnacademy.heukbaekfrontend.cart.service.impl;

import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateResponse;
import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final HashOperations<String, String, Integer> hashOperations;

    public CartServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }


    @Override
    public Map<String, Integer> getBooksFromCart(String sessionId) {
        return hashOperations.entries(sessionId);
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
}
