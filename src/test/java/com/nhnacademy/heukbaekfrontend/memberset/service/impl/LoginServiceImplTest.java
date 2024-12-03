//package com.nhnacademy.heukbaekfrontend.memberset.service.impl;
//
//import com.nhnacademy.heukbaekfrontend.memberset.member.client.LoginClient;
//import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
//import com.nhnacademy.heukbaekfrontend.memberset.member.service.impl.LoginServiceImpl;
//import feign.FeignException;
//import feign.Request;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class LoginServiceImplTest {
//
//    @InjectMocks
//    private LoginServiceImpl loginService;
//
//    @Mock
//    private LoginClient loginClient;
//
//    @Mock
//    private HttpServletResponse response;
//
//    @Test
//    void testLogin_Success() {
//        LoginRequest loginRequest = new LoginRequest("user", "password");
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.SET_COOKIE, "refreshToken=refresh_token_value");
//        headers.add(HttpHeaders.SET_COOKIE, "accessToken=access_token_value");
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("OK", headers, HttpStatus.OK);
//
//        when(loginClient.login(loginRequest)).thenReturn(responseEntity);
//
//        boolean result = loginService.login(loginRequest, response);
//
//        assertTrue(result);
//        verify(response).addHeader(HttpHeaders.SET_COOKIE, "refreshToken=refresh_token_value; HttpOnly; Secure; SameSite=Strict");
//        verify(response).addHeader(HttpHeaders.SET_COOKIE, "accessToken=access_token_value; HttpOnly; Secure; SameSite=Strict");
//    }
//
//    @Test
//    void testLogin_Success_NoCookies() {
//        LoginRequest loginRequest = new LoginRequest("user", "password");
//        HttpHeaders headers = new HttpHeaders();
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("OK", headers, HttpStatus.OK);
//
//        when(loginClient.login(loginRequest)).thenReturn(responseEntity);
//
//        boolean result = loginService.login(loginRequest, response);
//
//        assertTrue(result);
//        verify(response, never()).addHeader(eq(HttpHeaders.SET_COOKIE), anyString());
//    }
//
//    @Test
//    void testLogin_Non2xxStatus() {
//        LoginRequest loginRequest = new LoginRequest("user", "password");
//        HttpHeaders headers = new HttpHeaders();
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("Unauthorized", headers, HttpStatus.UNAUTHORIZED);
//
//        when(loginClient.login(loginRequest)).thenReturn(responseEntity);
//
//        boolean result = loginService.login(loginRequest, response);
//
//        assertFalse(result);
//        verify(response, never()).addHeader(eq(HttpHeaders.SET_COOKIE), anyString());
//    }
//
//    @Test
//    void testLogin_Unauthorized() {
//        LoginRequest loginRequest = new LoginRequest("user", "wrongPassword");
//        FeignException feignException = new FeignException.Unauthorized(
//                "Unauthorized",
//                Request.create(Request.HttpMethod.POST, "/login", Collections.emptyMap(), null, StandardCharsets.UTF_8, null),
//                null,
//                Collections.emptyMap());
//
//        when(loginClient.login(loginRequest)).thenThrow(feignException);
//
//        boolean result = loginService.login(loginRequest, response);
//
//        assertFalse(result);
//        verifyNoInteractions(response);
//    }
//
//    @Test
//    void testLogin_OtherException() {
//        LoginRequest loginRequest = new LoginRequest("user", "password");
//        FeignException feignException = new FeignException.InternalServerError(
//                "Internal Server Error",
//                Request.create(Request.HttpMethod.POST, "/login", Collections.emptyMap(), null, StandardCharsets.UTF_8, null),
//                null,
//                Collections.emptyMap());
//
//        when(loginClient.login(loginRequest)).thenThrow(feignException);
//
//        assertThrows(FeignException.class, () -> loginService.login(loginRequest, response));
//    }
//
//    @Test
//    void testAdminLogin_Success() {
//        LoginRequest loginRequest = new LoginRequest("admin", "adminPassword");
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.SET_COOKIE, "refreshToken=admin_refresh_token_value");
//        headers.add(HttpHeaders.SET_COOKIE, "accessToken=admin_access_token_value");
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("OK", headers, HttpStatus.OK);
//
//        when(loginClient.adminLogin(loginRequest)).thenReturn(responseEntity);
//
//        boolean result = loginService.adminLogin(loginRequest, response);
//
//        assertTrue(result);
//        verify(response).addHeader(HttpHeaders.SET_COOKIE, "refreshToken=admin_refresh_token_value; HttpOnly; Secure; SameSite=Strict");
//        verify(response).addHeader(HttpHeaders.SET_COOKIE, "accessToken=admin_access_token_value; HttpOnly; Secure; SameSite=Strict");
//    }
//
//    @Test
//    void testAdminLogin_Success_NoCookies() {
//        LoginRequest loginRequest = new LoginRequest("admin", "adminPassword");
//        HttpHeaders headers = new HttpHeaders();
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("OK", headers, HttpStatus.OK);
//
//        when(loginClient.adminLogin(loginRequest)).thenReturn(responseEntity);
//
//        boolean result = loginService.adminLogin(loginRequest, response);
//
//        assertTrue(result);
//        verify(response, never()).addHeader(eq(HttpHeaders.SET_COOKIE), anyString());
//    }
//
//    @Test
//    void testAdminLogin_Non2xxStatus() {
//        LoginRequest loginRequest = new LoginRequest("admin", "adminPassword");
//        HttpHeaders headers = new HttpHeaders();
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("Forbidden", headers, HttpStatus.FORBIDDEN);
//
//        when(loginClient.adminLogin(loginRequest)).thenReturn(responseEntity);
//
//        boolean result = loginService.adminLogin(loginRequest, response);
//
//        assertFalse(result);
//        verify(response, never()).addHeader(eq(HttpHeaders.SET_COOKIE), anyString());
//    }
//
//    @Test
//    void testAdminLogin_Unauthorized() {
//        LoginRequest loginRequest = new LoginRequest("admin", "wrongPassword");
//        FeignException feignException = new FeignException.Unauthorized(
//                "Unauthorized",
//                Request.create(Request.HttpMethod.POST, "/adminLogin", Collections.emptyMap(), null, StandardCharsets.UTF_8, null),
//                null,
//                Collections.emptyMap());
//
//        when(loginClient.adminLogin(loginRequest)).thenThrow(feignException);
//
//        boolean result = loginService.adminLogin(loginRequest, response);
//
//        assertFalse(result);
//        verifyNoInteractions(response);
//    }
//
//    @Test
//    void testAdminLogin_OtherException() {
//        LoginRequest loginRequest = new LoginRequest("admin", "adminPassword");
//        FeignException feignException = new FeignException.InternalServerError(
//                "Internal Server Error",
//                Request.create(Request.HttpMethod.POST, "/adminLogin", Collections.emptyMap(), null, StandardCharsets.UTF_8, null),
//                null,
//                Collections.emptyMap());
//
//        when(loginClient.adminLogin(loginRequest)).thenThrow(feignException);
//
//        assertThrows(FeignException.class, () -> loginService.adminLogin(loginRequest, response));
//    }
//}
