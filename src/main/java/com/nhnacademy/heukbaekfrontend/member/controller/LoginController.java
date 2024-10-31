package com.nhnacademy.heukbaekfrontend.member.controller;

import com.nhnacademy.heukbaekfrontend.member.dto.LoginRequest;
import com.nhnacademy.heukbaekfrontend.member.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String getLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute LoginRequest loginRequest, HttpServletResponse response) {
        boolean loginSuccess = loginService.login(loginRequest, response);

        return loginSuccess ? "redirect:/" : "redirect:/login?error=true";
    }

}
