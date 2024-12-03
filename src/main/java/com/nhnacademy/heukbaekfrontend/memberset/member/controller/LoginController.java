package com.nhnacademy.heukbaekfrontend.memberset.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    @GetMapping("/login")
    public String getLoginForm() {
        return "login/login";
    }
}
