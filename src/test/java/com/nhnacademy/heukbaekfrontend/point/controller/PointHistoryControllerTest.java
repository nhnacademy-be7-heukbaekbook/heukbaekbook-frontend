package com.nhnacademy.heukbaekfrontend.point.controller;

import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import com.nhnacademy.heukbaekfrontend.point.dto.PointHistoryResponse;
import com.nhnacademy.heukbaekfrontend.point.dto.PointType;
import com.nhnacademy.heukbaekfrontend.point.service.PointHistoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PointHistoryController.class)
class PointHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private PointHistoryService pointHistoryService;

    @MockBean
    private CookieUtil cookieUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new PointHistoryController(memberService,pointHistoryService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testGetMyPagePoints() throws Exception {

        MemberResponse mockMemberResponse = new MemberResponse(
                "John Doe", "010-1234-5678", "john.doe@example.com",
                "john_doe", null, null, null, null, null
        );
        BigDecimal mockBalance = BigDecimal.valueOf(1500);
        PointHistoryResponse mockPointHistory = new PointHistoryResponse(
                1L, 101L, 1001L, "Order Reward", BigDecimal.valueOf(100),
                LocalDateTime.now(), BigDecimal.valueOf(1500), PointType.EARNED
        );
        Page<PointHistoryResponse> mockPage = new PageImpl<>(
                Collections.singletonList(mockPointHistory),
                PageRequest.of(0, 10),
                1
        );


        when(memberService.getMember()).thenReturn(org.springframework.http.ResponseEntity.ok(mockMemberResponse));
        when(pointHistoryService.getCurrentPointBalance()).thenReturn(mockBalance);
        when(pointHistoryService.getPointHistories(PageRequest.of(0, 10))).thenReturn(mockPage);

        mockMvc.perform(get("/members/mypage/points")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("memberResponse", mockMemberResponse))
                .andExpect(model().attribute("balance", mockBalance))
                .andExpect(model().attribute("pointHistories", mockPage))
                .andExpect(view().name("mypage/mypage-points"));
    }

}
