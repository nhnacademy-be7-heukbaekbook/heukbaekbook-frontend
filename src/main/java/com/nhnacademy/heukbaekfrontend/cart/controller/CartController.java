package com.nhnacademy.heukbaekfrontend.cart.controller;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateRequest;
import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateResponse;
import com.nhnacademy.heukbaekfrontend.cart.dto.CartUpdateRequest;
import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 장바구니 컨트롤러
 *
 * @author 정동현
 * @since 2024-10-28
 */

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    // 장바구니에 있는 모든 책 조회
    @GetMapping
    public ModelAndView getBooksFromCart(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        log.info("sessionId: {}", sessionId);
        List<Book> cart = cartService.getBooksFromCart(sessionId);

        return new ModelAndView("/cart/cart-list").addObject("cart", cart);
    }

    // 장바구니에 책 조회
    @PostMapping
    public ResponseEntity<CartCreateResponse> createBookToCart(HttpServletRequest request,
                                                               @RequestBody CartCreateRequest cartCreateRequest) {
        String sessionId = request.getSession().getId();
        log.info("sessionId : {}, careCreateRequest : {}", sessionId, cartCreateRequest);
        CartCreateResponse cartCreateResponse = cartService.createBookToCart(sessionId, cartCreateRequest.bookId(), cartCreateRequest.quantity());
        return new ResponseEntity<>(cartCreateResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<String> updateBookQuantityFromCart(HttpServletRequest request,
                                                   @PathVariable Long bookId,
                                                   @RequestBody CartUpdateRequest cartUpdateRequest) {
        String sessionId = request.getSession().getId();
        log.info("sessionId : {}, bookId : {}, careUpdateRequest : {}", sessionId, bookId, cartUpdateRequest);
        cartService.updateBookQuantityFromCart(sessionId, bookId, cartUpdateRequest.quantity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBookFromCart(HttpServletRequest request,
                                                   @PathVariable Long bookId) {
        String sessionId = request.getSession().getId();
        log.info("sessionId : {}, careDeleteRequest : {}", sessionId, bookId);
        cartService.deleteBookFromCart(sessionId, bookId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
