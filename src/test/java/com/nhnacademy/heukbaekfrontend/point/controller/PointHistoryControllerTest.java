package com.nhnacademy.heukbaekfrontend.point.controller;

import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekfrontend.point.dto.PointHistoryResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(PointHistoryController.class)
@WithMockUser(username = "user", roles = {"USER"})
class PointHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private PointHistoryService pointHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMyPagePoints() throws Exception {
        // Mock 데이터 설정
        GradeDto mockGradeDto = new GradeDto("Silver", BigDecimal.valueOf(0.05), BigDecimal.valueOf(100000));
        BigDecimal mockBalance = BigDecimal.valueOf(50000);
        Page<PointHistoryResponse> mockPointHistories = new PageImpl<>(
                List.of(
                        new PointHistoryResponse(1L, 1L, 101L, "Order 101", BigDecimal.valueOf(1000),
                                LocalDateTime.now(), BigDecimal.valueOf(50000), PointType.EARNED),
                        new PointHistoryResponse(2L, 1L, 102L, "Order 102", BigDecimal.valueOf(-500),
                                LocalDateTime.now(), BigDecimal.valueOf(49500), PointType.USED)
                ), PageRequest.of(0, 10), 2
        );

        when(memberService.getMembersGrade()).thenReturn(Optional.of(mockGradeDto));
        when(pointHistoryService.getCurrentPointBalance()).thenReturn(mockBalance);
        when(pointHistoryService.getPointHistories(any())).thenReturn(mockPointHistories);

        // 요청 및 검증
        mockMvc.perform(get("/members/mypage/points"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("mypage/mypage-points"))
                .andExpect(model().attributeExists("gradeDto"))
                .andExpect(model().attribute("gradeDto", mockGradeDto))
                .andExpect(model().attributeExists("balance"))
                .andExpect(model().attribute("balance", mockBalance))
                .andExpect(model().attributeExists("pointHistories"))
                .andExpect(model().attribute("pointHistories", mockPointHistories));

        verify(memberService).getMembersGrade();
        verify(pointHistoryService).getCurrentPointBalance();
        verify(pointHistoryService).getPointHistories(any());
    }
}
