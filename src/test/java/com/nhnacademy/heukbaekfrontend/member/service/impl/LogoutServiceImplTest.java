package com.nhnacademy.heukbaekfrontend.member.service.impl;

import com.nhnacademy.heukbaekfrontend.member.client.AuthClient;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LogoutServiceImplTest {

    @Mock
    private AuthClient authClient;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private LogoutServiceImpl logoutService;

    @Test
    void testLogin() {
        logoutService.logout(response);

        verify(authClient, times(1)).logout();

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response, times(2)).addCookie(cookieCaptor.capture());

        assertEquals("accessToken", cookieCaptor.getAllValues().get(0).getName());
        assertEquals(0, cookieCaptor.getAllValues().get(0).getMaxAge());

        assertEquals("refreshToken", cookieCaptor.getAllValues().get(1).getName());
        assertEquals(0, cookieCaptor.getAllValues().get(1).getMaxAge());
    }
}
