//package com.nhnacademy.heukbaekfrontend.memberset.member.controller;
//
//import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
//import com.nhnacademy.heukbaekfrontend.memberset.member.dto.*;
//import com.nhnacademy.heukbaekfrontend.memberset.member.service.LogoutService;
//import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
//import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderBookResponse;
//import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderDetailResponse;
//import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.sql.Date;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(MemberController.class)
//class MemberControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private MemberService memberService;
//
//    @MockBean
//    private LogoutService logoutService;
//
//    private GradeDto gradeDto;
//
//    @BeforeEach
//    void setup() {
//        gradeDto = new GradeDto("Gold", BigDecimal.valueOf(10), BigDecimal.valueOf(1000));
//        when(memberService.getMembersGrade()).thenReturn(Optional.of(gradeDto));
//    }
//
//    @Test
//    @WithMockUser(username = "testUser", roles = "USER")
//    void testGetMyPageHome() throws Exception {
//        OrderResponse orderResponse = new OrderResponse(new PageImpl<>(List.of()));
//        MyPageResponse myPageResponse = new MyPageResponse(gradeDto, orderResponse);
//        PageRequest pageRequest = PageRequest.of(0, 10);
//
//        when(memberService.createMyPageResponse(pageRequest)).thenReturn(myPageResponse);
//
//        mockMvc.perform(get("/members/mypage"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("mypage/mypage"))
//                .andExpect(model().attributeExists("myPageResponse"))
//                .andExpect(model().attribute("myPageResponse", myPageResponse))
//                .andExpect(model().attributeExists("gradeDto"))
//                .andExpect(model().attribute("gradeDto", gradeDto));
//    }
//
//    @Test
//    @WithMockUser(username = "testUser", roles = "USER")
//    void testGetMyPageInfo() throws Exception {
//        MemberResponse memberResponse = new MemberResponse(
//                "John Doe",
//                "010-1234-5678",
//                "john.doe@example.com",
//                "john123",
//                Date.valueOf("1990-01-01"),
//                LocalDateTime.now(),
//                LocalDateTime.now(),
//                MemberStatus.ACTIVE,
//                gradeDto
//        );
//
//        when(memberService.getMember()).thenReturn(ResponseEntity.of(Optional.of(memberResponse)));
//
//        mockMvc.perform(get("/members/mypage/info"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("mypage/mypage-info"))
//                .andExpect(model().attributeExists("memberResponse"))
//                .andExpect(model().attribute("memberResponse", memberResponse))
//                .andExpect(model().attributeExists("gradeDto"))
//                .andExpect(model().attribute("gradeDto", gradeDto));
//    }
//
//    @Test
//    @WithMockUser(username = "testUser", roles = "USER")
//    void testDoUpdateMyPageInfo_Invalid() throws Exception {
//        MemberUpdateRequest invalidRequest = new MemberUpdateRequest(
//                "john123",
//                "short",
//                "short",
//                null,
//                "J",
//                "010-123",
//                "invalid-email"
//        );
//
//        mockMvc.perform(post("/members/mypage/info")
//                        .with(csrf())
//                        .flashAttr("memberUpdateRequest", invalidRequest))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/members/mypage/info"))
//                .andExpect(flash().attributeExists("error"));
//    }
//
//    @Test
//    @WithMockUser(username = "testUser", roles = "USER")
//    void testGetMyPageWithdraw() throws Exception {
//        MemberResponse memberResponse = new MemberResponse(
//                "John Doe",
//                "010-1234-5678",
//                "john.doe@example.com",
//                "john123",
//                Date.valueOf("1990-01-01"),
//                LocalDateTime.now(),
//                LocalDateTime.now(),
//                MemberStatus.ACTIVE,
//                gradeDto
//        );
//
//        when(memberService.getMember()).thenReturn(ResponseEntity.of(Optional.of(memberResponse)));
//
//        mockMvc.perform(get("/members/mypage/withdraw"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("mypage/mypage-withdraw"))
//                .andExpect(model().attributeExists("gradeDto"))
//                .andExpect(model().attribute("gradeDto", gradeDto));
//    }
//
//    @Test
//    @WithMockUser(username = "testUser", roles = "USER")
//    void testDoMemberWithdraw() throws Exception {
//        when(memberService.deleteMember()).thenReturn(true);
//
//        mockMvc.perform(post("/members/mypage/withdraw")
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/"));
//    }
//
//    @Test
//    @WithMockUser(username = "testUser", roles = "USER")
//    void testGetMyPageOrders() throws Exception {
//        OrderResponse orderResponse = new OrderResponse(new PageImpl<>(List.of()));
//        MyPageResponse myPageResponse = new MyPageResponse(gradeDto, orderResponse);
//        PageRequest pageRequest = PageRequest.of(0, 10);
//
//        when(memberService.createMyPageResponse(pageRequest)).thenReturn(myPageResponse);
//
//        mockMvc.perform(get("/members/mypage/orders"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("mypage/orders"))
//                .andExpect(model().attributeExists("myPageResponse"))
//                .andExpect(model().attribute("myPageResponse", myPageResponse))
//                .andExpect(model().attributeExists("gradeDto"))
//                .andExpect(model().attribute("gradeDto", gradeDto));
//    }
//
//    @Test
//    @WithMockUser(username = "testUser", roles = "USER")
//    void testGetMyPageOrderDetail() throws Exception {
//        List<OrderBookResponse> books = List.of(
//                new OrderBookResponse(1L, "thumbnailUrl", "Book Title", "10000", 2, "8000", BigDecimal.valueOf(20), "16000")
//        );
//
//        OrderDetailResponse orderDetailResponse = new OrderDetailResponse(
//                "ORDER123", "John Doe", "2500", "18500", "Card",
//                "Jane Doe", 12345L, "123 Main St", "Apt 101", "20000",
//                "4000", "18500", "Delivered", books
//        );
//
//        MyPageOrderDetailResponse myPageOrderDetailResponse = new MyPageOrderDetailResponse(gradeDto, orderDetailResponse);
//
//        when(memberService.getMyPageOrderDetail(any())).thenReturn(myPageOrderDetailResponse);
//
//        mockMvc.perform(get("/members/mypage/order/detail").param("orderId", "ORDER123"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("mypage/orderDetail"))
//                .andExpect(model().attributeExists("myPageOrderDetailResponse"))
//                .andExpect(model().attribute("myPageOrderDetailResponse", myPageOrderDetailResponse))
//                .andExpect(model().attributeExists("gradeDto"))
//                .andExpect(model().attribute("gradeDto", gradeDto));
//    }
//
//    @Test
//    @WithMockUser(username = "testUser", roles = "USER")
//    void testGetMyPageReviews() throws Exception {
//        mockMvc.perform(get("/members/mypage/reviews"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("mypage/reviews"))
//                .andExpect(model().attributeExists("gradeDto"))
//                .andExpect(model().attribute("gradeDto", gradeDto));
//    }
//}
