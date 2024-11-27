package com.nhnacademy.heukbaekfrontend.common.config;

import com.nhnacademy.heukbaekfrontend.common.client.AuthClient;
import com.nhnacademy.heukbaekfrontend.common.filter.AdminLoginFilter;
import com.nhnacademy.heukbaekfrontend.common.filter.MemberLoginFilter;
import com.nhnacademy.heukbaekfrontend.common.filter.ReissueFilter;
import com.nhnacademy.heukbaekfrontend.common.filter.TokenAuthenticationFilter;
import com.nhnacademy.heukbaekfrontend.oauth.client.PaycoClient;
import com.nhnacademy.heukbaekfrontend.oauth.client.PaycoTokenClient;
import com.nhnacademy.heukbaekfrontend.oauth.handler.CustomAuthenticationFailureHandler;
import com.nhnacademy.heukbaekfrontend.oauth.handler.CustomAuthenticationSuccessHandler;
import com.nhnacademy.heukbaekfrontend.oauth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.nhnacademy.heukbaekfrontend.oauth.resolver.CustomAuthorizationRequestResolver;
import com.nhnacademy.heukbaekfrontend.oauth.service.PaycoAuthorizationCodeTokenResponseClient;
import com.nhnacademy.heukbaekfrontend.oauth.service.PaycoOAuth2UserService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    private final PaycoClient paycoClient;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final PaycoTokenClient paycoTokenClient;

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
                                        .requestMatchers("/", "/payment/**", "/order/**", "/search", "/books/**").permitAll()
                                        .requestMatchers("/members/**").hasRole("MEMBER")
                                        .requestMatchers("/admin/**").hasRole("ADMIN")
                                        .requestMatchers("/logout").hasAnyRole("ADMIN", "MEMBER")
                                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                                .anyRequest().authenticated()
                );

        http
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository())
                                .authorizationRequestResolver(customAuthorizationRequestResolver())
                        )
                        .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                                .accessTokenResponseClient(paycoAccessTokenResponseClient())
                        )
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(paycoOAuth2UserService())
                        )
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
                );

//        http
//                .sessionManagement(session ->
//                        session.sessionFixation().migrateSession());
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .logout(logout -> logout
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

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> paycoOAuth2UserService() {
        return new PaycoOAuth2UserService(paycoClient);
    }

    @Bean
    public OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver() {
        return new CustomAuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> paycoAccessTokenResponseClient() {
        return new PaycoAuthorizationCodeTokenResponseClient(paycoTokenClient);
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> httpCookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository(cookieUtil);
    }
}
