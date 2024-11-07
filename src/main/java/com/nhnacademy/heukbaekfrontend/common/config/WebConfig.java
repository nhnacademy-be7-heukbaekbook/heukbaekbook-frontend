package com.nhnacademy.heukbaekfrontend.common.config;

import com.nhnacademy.heukbaekfrontend.common.interceptor.LoginStatusInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final LoginStatusInterceptor loginStatusInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginStatusInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(List.of("/css/**", "/images/**", "/js/**", "/login", "/admins/login", "/logout"));
    }
}
