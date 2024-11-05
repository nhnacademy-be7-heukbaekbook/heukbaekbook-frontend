package com.nhnacademy.heukbaekfrontend.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "authClient", url = "http://localhost:8082")
public interface AuthClient {

    @PostMapping("/api/auth/refresh")
    ResponseEntity<String> refreshTokens(@RequestHeader("Cookie") String refreshTokenCookie);
}
