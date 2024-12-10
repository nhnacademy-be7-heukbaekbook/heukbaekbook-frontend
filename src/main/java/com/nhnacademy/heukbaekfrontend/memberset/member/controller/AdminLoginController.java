package com.nhnacademy.heukbaekfrontend.memberset.member.controller;

import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class AdminLoginController {
    private final CookieUtil cookieUtil;

    @GetMapping("/admin/login")
    public String getAdminLoginForm(HttpServletRequest request, Model model) {
        String loginError = cookieUtil.getCookieValue(request, "loginError");
        if (loginError != null) {
            loginError = URLDecoder.decode(loginError, StandardCharsets.UTF_8);
        }
        model.addAttribute("errorMessage", loginError);
        return "login/adminLogin";
    }
}
