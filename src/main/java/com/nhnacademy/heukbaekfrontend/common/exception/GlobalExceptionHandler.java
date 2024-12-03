package com.nhnacademy.heukbaekfrontend.common.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.thymeleaf.exceptions.TemplateInputException;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({SecurityException.class, ForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleSecurityException(Exception ex, Model model) {
        model.addAttribute("statusCode", HttpStatus.FORBIDDEN.value());
        model.addAttribute("statusName", HttpStatus.FORBIDDEN.getReasonPhrase());
        model.addAttribute("errorMessage", Objects.isNull(ex.getMessage())
                ? "이 페이지에 접근할 권한이 없습니다." : ex.getMessage());

        return "error/error-page";
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequestException(Exception ex, Model model) {
        model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("statusName", HttpStatus.BAD_REQUEST.getReasonPhrase());
        model.addAttribute("errorMessage", Objects.isNull(ex.getMessage())
                ? "해당 페이지에 잘못된 요청입니다." : ex.getMessage());

        return "error/error-page";
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUnauthorizedException(Exception ex, Model model) {
        model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.value());
        model.addAttribute("statusName", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        model.addAttribute("errorMessage", Objects.isNull(ex.getMessage())
                ? "해당 페이지는 인증이 필요합니다." : ex.getMessage());

        return "error/error-page";
    }
//
//    @ExceptionHandler(ForbiddenException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public String handleForbiddenException(Exception ex, Model model) {
//        model.addAttribute("statusCode", HttpStatus.FORBIDDEN.value());
//        model.addAttribute("statusName", HttpStatus.FORBIDDEN.getReasonPhrase());
//        model.addAttribute("errorMessage", Objects.isNull(ex.getMessage())
//                ? "접근이 제한된 페이지입니다." : ex.getMessage());
//
//        return "error/error-page";
//    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception ex, Model model) {
        model.addAttribute("statusCode", HttpStatus.NOT_FOUND.value());
        model.addAttribute("statusName", HttpStatus.NOT_FOUND.getReasonPhrase());
        model.addAttribute("errorMessage", Objects.isNull(ex.getMessage())
                ? "요청하신 페이지를 찾을 수 없습니다." : ex.getMessage());

        return "error/error-page";
    }

    @ExceptionHandler(ServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleServerErrorException(Exception ex, Model model) {
        model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("statusName", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        model.addAttribute("errorMessage", Objects.isNull(ex.getMessage())
                ? "서버에 오류가 발생했습니다." : ex.getMessage());

        return "error/error-page";
    }


    @ExceptionHandler(TemplateInputException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleThymeleafException(Exception ex, Model model) {
        model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("statusName", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        model.addAttribute("errorMessage", Objects.isNull(ex.getMessage())
                ? "템플릿 처리 중 오류가 발생했습니다." : ex.getMessage());

        return "error/error-page";
    }
}
