package com.nhnacademy.heukbaekfrontend.common.config;

import com.nhnacademy.heukbaekfrontend.common.client.AuthClient;
import com.nhnacademy.heukbaekfrontend.common.filter.AdminLoginFilter;
import com.nhnacademy.heukbaekfrontend.common.filter.MemberLoginFilter;
import com.nhnacademy.heukbaekfrontend.common.filter.ReissueFilter;
import com.nhnacademy.heukbaekfrontend.common.filter.TokenAuthenticationFilter;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.common.util.JwtUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.client.LoginClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.client.LogoutClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.ACCESS_TOKEN;
import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.REFRESH_TOKEN;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginClient loginClient;
    private final AuthClient authClient;
    private final CookieUtil cookieUtil;
    private final LogoutClient logoutClient;
    private final JwtUtil jwtUtil;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http
                .authorizeHttpRequests(auth ->
                                auth
                                        .requestMatchers("/login", "/admin/login", "/signup/**", "/cart").permitAll()
                                        .requestMatchers("/", "/payment/**", "/order/**").permitAll()
                                        .requestMatchers("/members/**").hasRole("MEMBER")
                                        .requestMatchers("/admin/**").hasRole("ADMIN")
                                        .requestMatchers("/logout").hasAnyRole("ADMIN", "MEMBER")
                                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                                .anyRequest().authenticated()
                );

        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    logoutClient.logout();

                    cookieUtil.deleteCookie(response, ACCESS_TOKEN);
                    cookieUtil.deleteCookie(response, REFRESH_TOKEN);

                    response.sendRedirect("/");
                })
                .permitAll()
        );

        http.addFilterBefore(reissueFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(tokenAuthenticationFilter(), ReissueFilter.class);
        http.addFilterAt(memberLoginFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(adminLoginFilter(authenticationManager), MemberLoginFilter.class);

        return http.build();
    }

    @Bean
    public MemberLoginFilter memberLoginFilter(AuthenticationManager authenticationManager) {
        return new MemberLoginFilter(authenticationManager, loginClient, cookieUtil);
    }

    @Bean
    public AdminLoginFilter adminLoginFilter(AuthenticationManager authenticationManager) {
        return new AdminLoginFilter(authenticationManager, loginClient, cookieUtil);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(authClient, cookieUtil);
    }

    @Bean
    public ReissueFilter reissueFilter() {
        return new ReissueFilter(authClient, cookieUtil, jwtUtil);
    }
}
