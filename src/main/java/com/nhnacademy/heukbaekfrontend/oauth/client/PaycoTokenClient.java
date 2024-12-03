package com.nhnacademy.heukbaekfrontend.oauth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@FeignClient(name = "paycoTokenClient", url = "https://id.payco.com")
public interface PaycoTokenClient {

    @PostMapping(value = "/oauth2.0/token", consumes = "application/x-www-form-urlencoded")
    Map<String, Object> getAccessToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam(value = "state", required = false) String state
    );
}
