package com.nhnacademy.heukbaekfrontend.common.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoErrorDecoderConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder.Default();
    }
}
