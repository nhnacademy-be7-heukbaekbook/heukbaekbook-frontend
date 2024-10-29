package com.nhnacademy.heukbaekfrontend.cart.service;

import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateResponse;

import java.util.Map;


public interface CartService {

    Map<String, Integer> getBooksFromCart(String sessionId);

    CartCreateResponse createBookToCart(String sessionId, Long bookId, int quantity);

    void updateBookQuantityFromCart(String sessionId, Long bookId, int quantity);

    void deleteBookFromCart(String sessionId, Long bookId);
}
