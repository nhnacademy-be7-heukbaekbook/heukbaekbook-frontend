package com.nhnacademy.heukbaekfrontend.common.config;

import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TestConfig {

    @Bean
    public CookieUtil cookieUtil() {
        return new CookieUtil();
    }
}
