package com.nhnacademy.heukbaekfrontend.common.config;

import com.nhnacademy.heukbaekfrontend.common.config.keymanager.SecureKeyManagerResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PaycoEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String PAYCO_CLIENT_ID_KEY = "payco.client-id-key";
    private static final String PAYCO_CLIENT_SECRET_KEY = "payco.client-secret-key";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String paycoClientIdKey = environment.getProperty(PAYCO_CLIENT_ID_KEY);
        String paycoClientSecretKey = environment.getProperty(PAYCO_CLIENT_SECRET_KEY);

        // 키 매니저로부터 비밀 값 가져오기
        String paycoClientId = getSecret(paycoClientIdKey, environment);
        String paycoClientSecret = getSecret(paycoClientSecretKey, environment);

        // 프로퍼티 설정
        Map<String, Object> paycoProperties = new HashMap<>();
        paycoProperties.put("client.id", paycoClientId);
        paycoProperties.put("client.secret", paycoClientSecret);

        PropertySource<?> propertySource = new MapPropertySource("paycoProperties", paycoProperties);
        environment.getPropertySources().addFirst(propertySource);
    }

    private String getSecret(String keyId, Environment environment) {
        // 키 매니저로부터 비밀 값을 가져오는 로직 구현
        String appKey = environment.getProperty("nhncloud.keymanager.appKey");
        String userAccessKeyId = environment.getProperty("nhncloud.keymanager.userAccessKeyId");
        String secretAccessKey = environment.getProperty("nhncloud.keymanager.secretAccessKey");

        RestTemplate restTemplate = new RestTemplate();
        String endpoint = "https://api-keymanager.nhncloudservice.com";
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
