package com.nhnacademy.heukbaekfrontend.cart.service;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateResponse;
import com.nhnacademy.heukbaekfrontend.cart.dto.CartResponse;

import java.util.List;


public interface CartService {

    CartResponse getBooksFromCart(String sessionId);

    List<Book> getBooksByBookIdsFromCart(String sessionId, List<Long> bookIds);

    CartCreateResponse createBookToCart(String sessionId, Long bookId, int quantity);

    void updateBookQuantityFromCart(String sessionId, Long bookId, int quantity);

    void deleteBookFromCart(String sessionId, Long bookId);

    void synchronizeCartToDb(String sessionId);

    void synchronizeCartFromDb(String sessionId);
}
