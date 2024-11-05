package com.nhnacademy.heukbaekfrontend.member.controller;

import com.nhnacademy.heukbaekfrontend.member.service.LogoutService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LogoutController {

    private final LogoutService logoutService;

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        logoutService.logout(response);

        return "redirect:/";
    }
}
