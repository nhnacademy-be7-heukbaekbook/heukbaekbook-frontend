package com.nhnacademy.heukbaekfrontend.cart.controller;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateRequest;
import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateResponse;
import com.nhnacademy.heukbaekfrontend.cart.dto.CartUpdateRequest;
import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser // Mock 인증 사용자 추가
    void testGetBooksFromCart() throws Exception {
        List<Book> mockCart = List.of(
                new Book(1L, "Book A", true, 101L, "10000", "9000",
                        BigDecimal.valueOf(0.1), BigDecimal.valueOf(1000),
                        "http://example.com/thumbnail.jpg", 2,
                        "18,000", BigDecimal.valueOf(18000)),
                new Book(2L, "Book B", false, null, "20000", "18000",
                        BigDecimal.valueOf(0.1), BigDecimal.valueOf(2000),
                        "http://example.com/thumbnail_b.jpg", 1,
                        "18,000", BigDecimal.valueOf(18000))
        );

        when(cartService.getBooksFromCart(anyString())).thenReturn(mockCart);

        mockMvc.perform(get("/cart").sessionAttr("sessionId", "mockSessionId"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("cart/cart-list"))
                .andExpect(model().attributeExists("cart"))
                .andExpect(model().attribute("cart", mockCart));
    }

    @Test
    @WithMockUser // Mock 인증 사용자 추가
    void testCreateBookToCart() throws Exception {
        CartCreateRequest request = new CartCreateRequest(1L, 2);
        CartCreateResponse response = new CartCreateResponse(1L);

        when(cartService.createBookToCart(anyString(), eq(request.bookId()), eq(request.quantity())))
                .thenReturn(response);

        mockMvc.perform(post("/cart")
                        .sessionAttr("sessionId", "mockSessionId")
                        .with(csrf()) // CSRF 토큰 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "bookId": 1,
                                    "quantity": 2
                                }
                                """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookId").value(1));
    }

    @Test
    @WithMockUser // Mock 인증 사용자 추가
    void testUpdateBookQuantityFromCart() throws Exception {
        CartUpdateRequest request = new CartUpdateRequest(3);

        mockMvc.perform(put("/cart/1")
                        .sessionAttr("sessionId", "mockSessionId")
                        .with(csrf()) // CSRF 토큰 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "quantity": 3
                                }
                                """))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser // Mock 인증 사용자 추가
    void testDeleteBookFromCart() throws Exception {
        mockMvc.perform(delete("/cart/1")
                        .sessionAttr("sessionId", "mockSessionId")
                        .with(csrf())) // CSRF 토큰 추가
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
