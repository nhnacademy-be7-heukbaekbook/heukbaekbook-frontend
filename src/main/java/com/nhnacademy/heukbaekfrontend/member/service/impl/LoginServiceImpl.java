package com.nhnacademy.heukbaekfrontend.member.service.impl;

import com.nhnacademy.heukbaekfrontend.member.client.LoginClient;
import com.nhnacademy.heukbaekfrontend.member.dto.LoginRequest;
import com.nhnacademy.heukbaekfrontend.member.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final LoginClient loginClient;

    @Override
    public boolean login(LoginRequest loginRequest, HttpServletResponse response) {
        ResponseEntity<String> responseEntity = loginClient.login(loginRequest);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            List<String> cookies = responseEntity.getHeaders().get("Set-Cookie");
            if (cookies != null) {
                for (String cookie : cookies) {
                    String secureCookie = cookie + "; HttpOnly; Secure; SameSite=Strict";
                    response.addHeader("Set-Cookie", secureCookie);
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
