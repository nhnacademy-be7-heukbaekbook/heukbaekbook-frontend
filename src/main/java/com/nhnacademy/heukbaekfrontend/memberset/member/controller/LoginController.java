package com.nhnacademy.heukbaekfrontend.memberset.member.controller;

import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final CookieUtil cookieUtil;

    @GetMapping("/login")
    public String getLoginForm(HttpServletRequest request, Model model) {
        String loginError = cookieUtil.getCookieValue(request, "loginError");
        if (loginError != null) {
            loginError = URLDecoder.decode(loginError, StandardCharsets.UTF_8);
        }
        model.addAttribute("errorMessage", loginError);
        return "login/login";
    }
}
