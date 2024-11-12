package com.nhnacademy.heukbaekfrontend.common.client;

import com.nhnacademy.heukbaekfrontend.common.dto.TokenRequest;
import com.nhnacademy.heukbaekfrontend.common.dto.TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "authClient", url = "http://localhost:8082")
public interface AuthClient {

    @PostMapping("/api/auth/refresh")
    ResponseEntity<String> refreshTokens(@RequestHeader("Cookie") String refreshTokenCookie);

    @PostMapping("/api/auth/validate-admin")
    ResponseEntity<TokenResponse> validateAdmin(@RequestBody TokenRequest tokenRequest);

    @PostMapping("/api/auth/validate-member")
    ResponseEntity<TokenResponse> validateMember(@RequestBody TokenRequest tokenRequest);

}
