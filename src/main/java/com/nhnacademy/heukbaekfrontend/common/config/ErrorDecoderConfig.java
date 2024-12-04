//package com.nhnacademy.heukbaekfrontend.common.config;
//
//import com.nhnacademy.heukbaekfrontend.common.exception.*;
//import feign.Response;
//import feign.codec.ErrorDecoder;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Configuration
//public class ErrorDecoderConfig {
//
//    @Bean
//    public ErrorDecoder worldAuthErrorDecoder() {
//        return this::decode;
//    }
//
//
//    public Exception decode(String s, Response response) {
//        String errorMessage = extractErrorMessage(response);
//        int errorStatus = response.status();
//        log.info("{} 요청이 실패함. status: {}, body: {}", s, response.status(), response.body());
//
//        switch (errorStatus) {
//            case 400:
//                return new BadRequestException(errorMessage);  // 400 - Bad Request
//            case 401:
//                return new UnauthorizedException(errorMessage);  // 401 - Unauthorized
//            case 403:
//                return new ForbiddenException(errorMessage);  // 403 - Forbidden
//            case 404:
//                return new NotFoundException(errorMessage);  // 404 - Not Found
//            default:
//                if (errorStatus >= 500 && errorStatus < 600) {
//                    return new ServerErrorException(errorMessage);  // 500번대 - Server Error
//                }
//
//                return new Exception("Unknown error: " + errorMessage);
//        }
//    }
//
//
//    private String extractErrorMessage(Response response) {
//        if (response.body() == null) {
//            return "Just Feign Error. No other error message";
//        }
//
//        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.body().asInputStream()))) {
//            return bufferedReader.lines().collect(Collectors.joining("\n"));
//        } catch (Exception e) {
//            log.error("ERROR. Error reading response body", e);
//            return null;
//        }
//    }
//
//}
//
