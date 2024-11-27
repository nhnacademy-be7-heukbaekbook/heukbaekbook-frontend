package com.nhnacademy.heukbaekfrontend.oauth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "paycoClient", url = "https://apis-payco.krp.toastoven.net")
public interface PaycoClient {

    @PostMapping(value = "/payco/friends/find_member_v2.json", consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> getUserInfo(@RequestHeader("client_id") String clientId,
                                    @RequestHeader("access_token") String accessToken);
}
