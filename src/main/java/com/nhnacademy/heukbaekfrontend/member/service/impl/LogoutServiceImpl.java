package com.nhnacademy.heukbaekfrontend.member.service.impl;

import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.member.client.LogoutClient;
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
    private final LogoutClient logoutClient;
    private final CookieUtil cookieUtil;

    @Override
    public void logout(HttpServletResponse response) {
        logoutClient.logout();

        cookieUtil.deleteCookie(response, ACCESS_TOKEN);
        cookieUtil.deleteCookie(response, REFRESH_TOKEN);
    }
}
