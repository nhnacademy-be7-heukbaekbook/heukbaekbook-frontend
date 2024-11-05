package com.nhnacademy.heukbaekfrontend.member.service.impl;

import com.nhnacademy.heukbaekfrontend.member.client.AuthClient;
import com.nhnacademy.heukbaekfrontend.member.service.LogoutService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.ACCESS_TOKEN;
import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {
    private final AuthClient authClient;

    @Override
    public void logout(HttpServletResponse response) {
        authClient.logout();

        clearCookie(response, ACCESS_TOKEN);
        clearCookie(response, REFRESH_TOKEN);
    }

    private void clearCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
