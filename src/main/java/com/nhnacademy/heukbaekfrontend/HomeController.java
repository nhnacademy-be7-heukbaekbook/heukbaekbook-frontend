package com.nhnacademy.heukbaekfrontend;

import com.nhnacademy.heukbaekfrontend.book.dto.response.BookResponse;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.ACCESS_TOKEN;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CookieUtil cookieUtil;

    private final BookService bookService;


    @GetMapping("/")
    public ModelAndView home(HttpServletRequest request) {
        String accessToken = cookieUtil.getCookie(request, ACCESS_TOKEN);
        boolean isLogin = accessToken != null;

        ModelAndView modelAndView = new ModelAndView("home");
        Page<BookResponse> page = bookService.getBooks(PageRequest.of(0, 25));
        modelAndView.addObject("page", page).addObject("isLogin", isLogin);
        return modelAndView;
    }
}
