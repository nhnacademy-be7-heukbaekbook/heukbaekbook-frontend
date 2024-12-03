//package com.nhnacademy.heukbaekfrontend.memberset.address.controller;
//
//import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
//import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressRequest;
//import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressResponse;
//import com.nhnacademy.heukbaekfrontend.memberset.address.service.MemberAddressService;
//import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
//import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
//import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberStatus;
//import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//
//import java.math.BigDecimal;
//import java.sql.Date;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(MemberAddressController.class)
//class MemberAddressControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private MemberAddressService memberAddressService;
//
//    @MockBean
//    private MemberService memberService;
//
//    @MockBean
//    private CookieUtil cookieUtil;
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setPrefix("/WEB-INF/views/");
//        viewResolver.setSuffix(".jsp");
//
//        mockMvc = MockMvcBuilders.standaloneSetup(new MemberAddressController(memberAddressService, memberService))
//                .setViewResolvers(viewResolver)
//                .build();
//    }
//
//    @Test
//    void testGetMyPageAddresses() throws Exception {
//        List<MemberAddressResponse> addressList = List.of(
//                new MemberAddressResponse(1L, 1L, "City", "State", "12345"),
//                new MemberAddressResponse(2L, 2L, "City", "State", "67890")
//        );
//
//        GradeDto gradeDto = new GradeDto(
//                "Gold",
//                new BigDecimal("15.00"),
//                new BigDecimal("1000.00")
//        );
//
//        MemberResponse memberResponse = new MemberResponse(
//                "John Doe",
//                "010-1234-5678",
//                "john.doe@example.com",
//                "johndoe",
//                Date.valueOf("1990-01-01"),
//                LocalDateTime.of(2023, 1, 1, 10, 0),
//                LocalDateTime.of(2024, 1, 1, 12, 0),
//                MemberStatus.ACTIVE,
//                gradeDto
//        );
//
//        when(memberAddressService.getMemberAddressesList()).thenReturn(addressList);
//        when(memberService.getMember()).thenReturn(ResponseEntity.ok(memberResponse));
//
//        mockMvc.perform(get("/members/mypage/addresses"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("mypage/mypage-address"))
//                .andExpect(model().attributeExists("memberResponse"))
//                .andExpect(model().attributeExists("addressList"))
//                .andExpect(model().attribute("memberResponse", memberResponse))
//                .andExpect(model().attribute("addressList", addressList));
//    }
//
//
//
//    @Test
//    void testCountMemberAddresses() throws Exception {
//        when(memberAddressService.countMemberAddresses()).thenReturn(ResponseEntity.ok(2L));
//
//        mockMvc.perform(get("/members/mypage/addresses/count"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("2"));
//    }
//
//    @Test
//    void testAddMemberAddress() throws Exception {
//        MemberAddressRequest request = new MemberAddressRequest(1L, "City", "State", "12345");
//
//        mockMvc.perform(post("/members/mypage/addresses")
//                        .flashAttr("memberAddressRequest", request))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/members/mypage/addresses"));
//    }
//
//    @Test
//    void testUpdateMemberAddress() throws Exception {
//        MemberAddressRequest request = new MemberAddressRequest(1L, "New City", "New State", "67890");
//
//        mockMvc.perform(put("/members/mypage/addresses/1")
//                        .flashAttr("memberAddressRequest", request))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/members/mypage/addresses"));
//    }
//
//    @Test
//    void testDeleteMemberAddress() throws Exception {
//        mockMvc.perform(delete("/members/mypage/addresses/1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/members/mypage/addresses"));
//    }
//}
