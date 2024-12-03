//package com.nhnacademy.heukbaekfrontend.memberset.member.controller;
//
//import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
//import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
//import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberUpdateRequest;
//import com.nhnacademy.heukbaekfrontend.memberset.member.service.LogoutService;
//import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(MemberController.class)
//class MemberControllerTest {
//
//    private MockMvc mockMvc;
//
//    @MockBean
//    private MemberService memberService;
//
//    @MockBean
//    private LogoutService logoutService;
//
//    @MockBean
//    private CookieUtil cookieUtil;
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(new MemberController(memberService, logoutService))
//                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
//                .build();
//    }
//
////    @Test
////    void testGetMyPageHome() throws Exception {
////        MemberResponse mockResponse = new MemberResponse(
////                "John Doe", "010-1234-5678", "john.doe@example.com",
////                "john_doe", null, null, null, null, null
////        );
////        when(memberService.getMember()).thenReturn(ResponseEntity.ok(mockResponse));
////
////        mockMvc.perform(get("/members/mypage"))
////                .andExpect(status().isOk())
////                .andExpect(model().attribute("memberResponse", mockResponse))
////                .andExpect(view().name("mypage/mypage"));
////    }
//
//    @Test
//    void testGetMyPageInfo() throws Exception {
//        MemberResponse mockResponse = new MemberResponse(
//                "John Doe", "010-1234-5678", "john.doe@example.com",
//                "john_doe", null, null, null, null, null
//        );
//        when(memberService.getMember()).thenReturn(ResponseEntity.ok(mockResponse));
//
//        mockMvc.perform(get("/members/mypage/info"))
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("memberResponse", mockResponse))
//                .andExpect(view().name("mypage/mypage-info"));
//    }
//
//    @Test
//    void testDoUpdateMyPageInfo_Success() throws Exception {
//        MemberResponse mockResponse = new MemberResponse(
//                "John Doe", "010-1234-5678", "john.doe@example.com",
//                "john_doe", null, null, null, null, null
//        );
//        when(memberService.updateMember(any(MemberUpdateRequest.class))).thenReturn(java.util.Optional.of(mockResponse));
//
//        mockMvc.perform(post("/members/mypage/info")
//                        .param("name", "John Doe")
//                        .param("phoneNumber", "010-1234-5678")
//                        .param("email", "john.doe@example.com"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/members/mypage/info"));
//    }
//
//    @Test
//    void testDoMemberWithdraw_Success() throws Exception {
//        when(memberService.deleteMember()).thenReturn(true);
//        doNothing().when(logoutService).logout(any());
//
//        mockMvc.perform(post("/members/mypage/withdraw"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/"));
//    }
//}
