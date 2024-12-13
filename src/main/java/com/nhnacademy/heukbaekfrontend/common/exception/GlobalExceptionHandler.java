package com.nhnacademy.heukbaekfrontend.common.exception;

import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.exception.CouponIssueTimeException;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.exception.CouponQuntatiyException;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.exception.DuplicatedCouponException;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.thymeleaf.exceptions.TemplateInputException;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String STATUS_CODE = "statusCode";
    private static final String STATUS_NAME = "statusName";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String ERROR_PAGE = "error/error-page";

    @ExceptionHandler({SecurityException.class, ForbiddenException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleSecurityException(Exception ex, Model model) {
        model.addAttribute(STATUS_CODE, HttpStatus.FORBIDDEN.value());
        model.addAttribute(STATUS_NAME, HttpStatus.FORBIDDEN.getReasonPhrase());
        model.addAttribute(ERROR_MESSAGE, Objects.isNull(ex.getMessage())
                ? "이 페이지에 접근할 권한이 없습니다." : ex.getMessage());

        return ERROR_PAGE;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequestException(Exception ex, Model model) {
        model.addAttribute(STATUS_CODE, HttpStatus.BAD_REQUEST.value());
        model.addAttribute(STATUS_NAME, HttpStatus.BAD_REQUEST.getReasonPhrase());
        model.addAttribute(ERROR_MESSAGE, Objects.isNull(ex.getMessage())
                ? "해당 페이지에 잘못된 요청입니다." : ex.getMessage());

        return ERROR_PAGE;
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUnauthorizedException(Exception ex, Model model) {
        model.addAttribute(STATUS_CODE, HttpStatus.UNAUTHORIZED.value());
        model.addAttribute(STATUS_NAME, HttpStatus.UNAUTHORIZED.getReasonPhrase());
        model.addAttribute(ERROR_MESSAGE, Objects.isNull(ex.getMessage())
                ? "해당 페이지는 인증이 필요합니다." : ex.getMessage());

        return ERROR_PAGE;
    }

    @ExceptionHandler({NotFoundException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception ex, Model model) {
        model.addAttribute(STATUS_CODE, HttpStatus.NOT_FOUND.value());
        model.addAttribute(STATUS_NAME, HttpStatus.NOT_FOUND.getReasonPhrase());
        model.addAttribute(ERROR_MESSAGE, Objects.isNull(ex.getMessage())
                ? "요청하신 페이지를 찾을 수 없습니다." : ex.getMessage());

        return ERROR_PAGE;
    }

    @ExceptionHandler(ServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleServerErrorException(Exception ex, Model model) {
        model.addAttribute(STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute(STATUS_NAME, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        model.addAttribute(ERROR_MESSAGE, Objects.isNull(ex.getMessage())
                ? "서버에 오류가 발생했습니다." : ex.getMessage());

        return ERROR_PAGE;
    }

    @ExceptionHandler(TemplateInputException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleThymeleafException(Exception ex, Model model) {
        model.addAttribute(STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute(STATUS_NAME, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        model.addAttribute(ERROR_MESSAGE, Objects.isNull(ex.getMessage())
                ? "템플릿 처리 중 오류가 발생했습니다." : ex.getMessage());

        return ERROR_PAGE;
    }

    @ExceptionHandler(DuplicatedCouponException.class)
    public ResponseEntity<String> handleDuplicatedCouponException(DuplicatedCouponException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CouponIssueTimeException.class)
    public ResponseEntity<String> handleCouponIssueTimeException(CouponIssueTimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CouponQuntatiyException.class)
    public ResponseEntity<String> handleCouponQuntatiyException(CouponQuntatiyException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
