package com.nhnacademy.heukbaekfrontend.cart.controller;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateRequest;
import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateResponse;
import com.nhnacademy.heukbaekfrontend.cart.dto.CartUpdateRequest;
import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private CookieUtil cookieUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new CartController(cartService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testGetBooksFromCart() throws Exception {
        List<Book> mockCart = List.of(
                new Book(1L, "Test Book", true,"1000", "800", BigDecimal.valueOf(20),
                        new BigDecimal("200"), "http://example.com/thumbnail.jpg",
                        2, "₩1,600", new BigDecimal("1600"))
        );
        when(cartService.getBooksFromCart(anyString())).thenReturn(mockCart);

        mockMvc.perform(get("/cart")
                        .sessionAttr("sessionId", "mock-session-id"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart/cart-list"))
                .andExpect(model().attributeExists("cart"))
                .andExpect(model().attribute("cart", mockCart));
    }

    @Test
    void testCreateBookToCart() throws Exception {

        CartCreateRequest cartCreateRequest = new CartCreateRequest(1L, 2);
        CartCreateResponse cartCreateResponse = new CartCreateResponse(1L);

        when(cartService.createBookToCart(anyString(), eq(1L), eq(2))).thenReturn(cartCreateResponse);

        mockMvc.perform(post("/cart")
                        .sessionAttr("sessionId", "mock-session-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookId\":1,\"quantity\":2}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookId").value(1L));
    }


    @Test
    void testUpdateBookQuantityFromCart() throws Exception {
        CartUpdateRequest cartUpdateRequest = new CartUpdateRequest(5);

        doNothing().when(cartService).updateBookQuantityFromCart(anyString(), eq(1L), eq(5));

        mockMvc.perform(put("/cart/1")
                        .sessionAttr("sessionId", "mock-session-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":5}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteBookFromCart() throws Exception {
        doNothing().when(cartService).deleteBookFromCart(anyString(), eq(1L));

        mockMvc.perform(delete("/cart/1")
                        .sessionAttr("sessionId", "mock-session-id"))
                .andExpect(status().isNoContent());
    }
}
