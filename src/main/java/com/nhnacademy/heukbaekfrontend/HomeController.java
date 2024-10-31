package com.nhnacademy.heukbaekfrontend;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@EnableFeignClients
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        model.addAttribute("ip", ip);
        return "home";
    }
}
