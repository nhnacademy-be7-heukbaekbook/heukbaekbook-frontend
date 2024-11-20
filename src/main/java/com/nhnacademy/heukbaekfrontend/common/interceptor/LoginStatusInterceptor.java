package com.nhnacademy.heukbaekfrontend.common.interceptor;

import com.nhnacademy.heukbaekfrontend.common.annotation.Member;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.ACCESS_TOKEN;

@Component
@RequiredArgsConstructor
public class LoginStatusInterceptor implements HandlerInterceptor {
    private final CookieUtil cookieUtil;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            String accessToken = cookieUtil.getCookie(request, ACCESS_TOKEN);
            boolean isLogin = (accessToken != null);
            modelAndView.addObject("isLogin", isLogin);
        }
    }
}
