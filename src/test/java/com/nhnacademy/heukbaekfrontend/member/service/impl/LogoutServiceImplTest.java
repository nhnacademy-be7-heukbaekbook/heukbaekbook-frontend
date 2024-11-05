package com.nhnacademy.heukbaekfrontend.member.service.impl;

import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.member.client.LogoutClient;
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
    private LogoutClient logoutClient;

    @Mock
    private HttpServletResponse response;

    @Mock
    private CookieUtil cookieUtil;

    @InjectMocks
    private LogoutServiceImpl logoutService;

    @Test
    void testLogout() {
        logoutService.logout(response);

        verify(logoutClient, times(1)).logout();
        verify(cookieUtil, times(1)).deleteCookie(response, "accessToken");
        verify(cookieUtil, times(1)).deleteCookie(response, "refreshToken");
    }
}
