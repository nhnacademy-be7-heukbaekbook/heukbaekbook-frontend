package com.nhnacademy.heukbaekfrontend.memberset.member.client;

import com.nhnacademy.heukbaekfrontend.common.dto.LoginResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "loginClient", url = "http://localhost:8082")
public interface LoginClient {

    @PostMapping("/api/auth/login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest);

    @PostMapping("/api/auth/admin/login")
    ResponseEntity<LoginResponse> adminLogin(@RequestBody LoginRequest loginRequest);
}
