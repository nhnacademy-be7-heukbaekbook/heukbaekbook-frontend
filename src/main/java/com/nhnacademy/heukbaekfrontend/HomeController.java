package com.nhnacademy.heukbaekfrontend;

import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.ACCESS_TOKEN;

@Controller
@EnableFeignClients
@RequiredArgsConstructor
public class HomeController {
    private final CookieUtil cookieUtil;

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
//        String accessToken = cookieUtil.getCookie(request, ACCESS_TOKEN);
//        boolean isLogin = accessToken != null;
//
//        model.addAttribute("isLogin", isLogin);
        return "home";
    }
}
