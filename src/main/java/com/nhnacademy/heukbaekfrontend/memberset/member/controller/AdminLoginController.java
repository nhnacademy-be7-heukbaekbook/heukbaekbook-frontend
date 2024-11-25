package com.nhnacademy.heukbaekfrontend.memberset.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminLoginController {

    @GetMapping("/admin/login")
    public String getAdminLoginForm() {
        return "login/adminLogin";
    }
}
