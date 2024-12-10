package com.nhnacademy.heukbaekfrontend.common.config.keymanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SecureKeyManagerConfigTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ConfigurableEnvironment env;

    @InjectMocks
    private SecureKeyManagerConfig secureKeyManagerConfig;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // 리플렉션을 사용하여 private 필드 값을 설정
        setPrivateField(secureKeyManagerConfig, "appKey", "testAppKey");
        setPrivateField(secureKeyManagerConfig, "userAccessKeyId", "testUserAccessKeyId");
        setPrivateField(secureKeyManagerConfig, "secretAccessKey", "testSecretAccessKey");
    }

    @Test
    void getSecret_ShouldHandleNullBodyGracefully() {
        // Arrange
        String keyId = "testKeyId";
        String url = String.format("https://api-keymanager.nhncloudservice.com/keymanager/v1.2/appkey/%s/secrets/%s",
                "testAppKey", keyId);

        SecureKeyManagerResponse mockResponse = new SecureKeyManagerResponse(
                new Header(200, "Success", true),
                null // Body가 null인 경우
        );

        ResponseEntity<SecureKeyManagerResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(SecureKeyManagerResponse.class)))
                .thenReturn(responseEntity);

        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> secureKeyManagerConfig.getSecret(keyId));
        assertThat(exception.getMessage()).contains("Cannot invoke");
    }


    @Test
    void getSecret_ShouldThrowException_WhenResponseBodyIsNull() {
        // Arrange
        String keyId = "testKeyId";
        String url = String.format("https://api-keymanager.nhncloudservice.com/keymanager/v1.2/appkey/%s/secrets/%s",
                "testAppKey", keyId);

        SecureKeyManagerResponse mockResponse = new SecureKeyManagerResponse(
                new Header(200, "Success", true),
                null
        );

        ResponseEntity<SecureKeyManagerResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(SecureKeyManagerResponse.class)))
                .thenReturn(responseEntity);

        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> secureKeyManagerConfig.getSecret(keyId));
        assertThat(exception.getMessage()).contains("Cannot invoke");
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // private 필드 접근 가능하게 설정
        field.set(target, value); // 필드 값 설정
    }
}
