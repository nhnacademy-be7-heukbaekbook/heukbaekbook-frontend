package com.nhnacademy.heukbaekfrontend.memberset.member.service.impl;

import com.nhnacademy.heukbaekfrontend.memberset.member.client.LoginClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.LoginService;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private static final String COOKIE_ATTRIBUTES = "; HttpOnly; Secure; SameSite=Strict";

    private final LoginClient loginClient;

    @Override
    public boolean login(LoginRequest loginRequest, HttpServletResponse response) {
        return true;
//        return executeLogin(() -> loginClient.login(loginRequest), response);
    }

    @Override
    public boolean adminLogin(LoginRequest loginRequest, HttpServletResponse response) {
        return true;
//        return executeLogin(() -> loginClient.adminLogin(loginRequest), response);
    }

    private boolean executeLogin(Supplier<ResponseEntity<String>> loginFunction, HttpServletResponse response) {
        try {
            ResponseEntity<String> responseEntity = loginFunction.get();
            return processLoginResponse(responseEntity, response);
        } catch (FeignException fe) {
            if (fe.status() == UNAUTHORIZED.value()) {
                return false;
            } else {
                throw fe;
            }
        }
    }

    private boolean processLoginResponse(ResponseEntity<String> responseEntity, HttpServletResponse response) {
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            List<String> cookies = responseEntity.getHeaders().get(SET_COOKIE);
            if (cookies != null) {
                for (String cookie : cookies) {
                    String secureCookie = cookie + COOKIE_ATTRIBUTES;
                    response.addHeader(SET_COOKIE, secureCookie);
                }
            }
            return true;
        }
        return false;
    }
}
