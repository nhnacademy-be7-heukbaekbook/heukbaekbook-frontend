package com.nhnacademy.heukbaekfrontend.point.controller;

import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.order.controller.OrderController;
import com.nhnacademy.heukbaekfrontend.point.dto.*;
import com.nhnacademy.heukbaekfrontend.point.service.PointEarnStandardService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PointEarnStandardController.class)
class PointEarnStandardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointEarnStandardService pointEarnStandardService;

    @MockBean
    private CookieUtil cookieUtil;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new PointEarnStandardController(pointEarnStandardService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testGetPointEarnStandard() throws Exception {
        Map<EventCode, List<PointEarnStandardResponse>> standardsByEvent = new EnumMap<>(EventCode.class);
        standardsByEvent.put(EventCode.LOGIN, Collections.singletonList(
                new PointEarnStandardResponse(
                        1L,
                        "Login Bonus",
                        BigDecimal.valueOf(50),
                        PointEarnStandardStatus.ACTIVATED,
                        PointEarnType.FIXED,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(7),
                        EventCode.LOGIN
                )
        ));
        when(pointEarnStandardService.getValidStandardsByEvent("LOGIN"))
                .thenReturn(standardsByEvent.get(EventCode.LOGIN));

        mockMvc.perform(get("/admin/point-earn-standard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("triggerEvents", "pointEarnTypes", "standardsByEvent"))
                .andExpect(view().name("point/admin/point-earn-standard"));
    }

    @Test
    void testSavePointEarnStandard() throws Exception {
        PointEarnStandardResponse response = new PointEarnStandardResponse(
                1L,
                "Signup Bonus",
                BigDecimal.valueOf(100),
                PointEarnStandardStatus.ACTIVATED,
                PointEarnType.FIXED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                EventCode.SIGNUP
        );
        when(pointEarnStandardService.createPointEarnStandard(any(PointEarnStandardRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/admin/point-earn-standard")
                        .param("name", "Signup Bonus")
                        .param("point", "100")
                        .param("status", "ACTIVATED")
                        .param("pointEarnType", "FIXED")
                        .param("pointEarnStart", LocalDateTime.now().toString())
                        .param("pointEarnEnd", LocalDateTime.now().plusDays(30).toString())
                        .param("eventCode", "SIGNUP"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/point-earn-standard"));
    }

    @Test
    void testDeletePointEarnStandard() throws Exception {
        mockMvc.perform(delete("/admin/point-earn-standard/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/point-earn-standard"));
    }

    @Test
    void testUpdatePointEarnStandard() throws Exception {
        PointEarnStandardResponse response = new PointEarnStandardResponse(
                1L,
                "Updated Bonus",
                BigDecimal.valueOf(150),
                PointEarnStandardStatus.ACTIVATED,
                PointEarnType.PERCENTAGE,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                EventCode.ORDER
        );
        when(pointEarnStandardService.updatePointEarnStandard(any(Long.class), any(PointEarnStandardRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/admin/point-earn-standard/1")
                        .param("name", "Updated Bonus")
                        .param("point", "150")
                        .param("status", "ACTIVATED")
                        .param("pointEarnType", "PERCENTAGE")
                        .param("pointEarnStart", LocalDateTime.now().toString())
                        .param("pointEarnEnd", LocalDateTime.now().plusDays(30).toString())
                        .param("eventCode", "ORDER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/point-earn-standard"));
    }
}

