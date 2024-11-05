package com.nhnacademy.heukbaekfrontend.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class SecureKeyManagerConfig {

    @Value("${nhncloud.keymanager.appKey}")
    private String appKey;

    @Value("${nhncloud.keymanager.userAccessKeyId}")
    private String userAccessKeyId;

    @Value("${nhncloud.keymanager.secretAccessKey}")
    private String secretAccessKey;

    private final String endpoint = "https://api-keymanager.nhncloudservice.com";

    public String getSecret(String keyId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s/keymanager/v1.2/appkey/%s/secrets/%s", endpoint, appKey, keyId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-TC-AUTHENTICATION-ID", userAccessKeyId);
        headers.set("X-TC-AUTHENTICATION-SECRET", secretAccessKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<SecureKeyManagerResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, SecureKeyManagerResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(response.getBody()).body().secret();
        } else {
            throw new RuntimeException("기밀 데이터 조회 실패: " + response.getStatusCode());
        }
    }
}
