package com.nhnacademy.heukbaekfrontend.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CookieUtilTest {

    @InjectMocks
    private CookieUtil cookieUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    void testGetCookie_ReturnsCookieValue() {
        Cookie cookie = new Cookie("testCookie", "testValue");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        String result = cookieUtil.getCookie(request, "testCookie");

        assertEquals("testValue", result);
    }

    @Test
    void testGetCookie_ReturnsNullWhenCookieNotFound() {
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("otherCookie", "otherValue")});

        String result = cookieUtil.getCookie(request, "testCookie");

        assertNull(result);
    }

    @Test
    void testGetCookie_ReturnsNullWhenNoCookiesPresent() {
        when(request.getCookies()).thenReturn(null);

        String result = cookieUtil.getCookie(request, "testCookie");

        assertNull(result);
    }

    @Test
    void testDeleteCookie_AddsExpiredCookieToResponse() {
        cookieUtil.deleteCookie(response, "testCookie");

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());

        Cookie deletedCookie = cookieCaptor.getValue();
        assertEquals("testCookie", deletedCookie.getName());
        assertEquals(0, deletedCookie.getMaxAge());
        assertEquals("/", deletedCookie.getPath());
        assertNull(deletedCookie.getValue());
    }
}
